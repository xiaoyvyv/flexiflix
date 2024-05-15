@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.xiaoyv.flexiflix.extension.java

import com.android.tools.smali.dexlib2.Opcodes
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile
import com.xiaoyv.flexiflix.extension.BuildConfig
import com.xiaoyv.flexiflix.extension.ExtensionProvider
import com.xiaoyv.flexiflix.extension.java.annotation.MediaSource
import com.xiaoyv.flexiflix.extension.java.model.MediaSourceInfo
import com.xiaoyv.flexiflix.extension.java.network.converter.WebDocumentConverter
import com.xiaoyv.flexiflix.extension.java.network.converter.WebHtmlConverter
import com.xiaoyv.flexiflix.extension.java.network.cookie.PersistentCookieJar
import com.xiaoyv.flexiflix.extension.java.network.cookie.cache.SetCookieCache
import com.xiaoyv.flexiflix.extension.java.network.cookie.persistence.SharedPrefsCookiePersistor
import com.xiaoyv.flexiflix.extension.java.network.dns.NetworkManager
import com.xiaoyv.flexiflix.extension.java.network.interceptor.AdBlockInterceptor
import com.xiaoyv.flexiflix.extension.java.network.interceptor.CommonInterceptor
import com.xiaoyv.flexiflix.extension.java.source.HttpSource
import com.xiaoyv.flexiflix.extension.java.source.Source
import com.xiaoyv.flexiflix.extension.java.utils.jvmSignature
import com.xiaoyv.flexiflix.extension.java.utils.md5
import dalvik.system.PathClassLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


/**
 * [MediaSourceFactory]
 *
 * @author why
 * @since 5/8/24
 */
object MediaSourceFactory {
    private val cacheMap = ConcurrentHashMap<String, List<MediaSourceExtension>>()

    private val parentClassLoader by lazy {
        javaClass.classLoader ?: ClassLoader.getSystemClassLoader()
    }

    /**
     * 网络请求 Cookie 持久化
     *
     * 注意：如果移动了 [PersistentCookieJar] 相关类的位置，导致其包名变了后，需要清空SP的缓存数据，否则反序列化会报错！
     */
    val cookieJar: CookieJar by lazy {
        PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(
                requireNotNull(ExtensionProvider.application)
            )
        )
    }

    /**
     * 网络请求客户端，开发数据源时，可以直接使用该实例副本
     */
    val okhttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .callTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
            }
//            .dns(NetworkDns())
            .addInterceptor(CommonInterceptor())
            .addNetworkInterceptor(AdBlockInterceptor())
            .build()
    }

    /**
     * 此 Retrofit 有 Cookie 持久化
     *
     * 使用时直接可以 retrofit.newBuilder() 传入 BaseUrl 进行构建
     */
    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .addConverterFactory(WebHtmlConverter.create())
            .addConverterFactory(WebDocumentConverter.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttp)
    }

    /**
     * 此 Retrofit 不会自动重定向，其余的同 [retrofitBuilder]
     *
     *
     * 使用时直接可以 retrofitNoRedirect.newBuilder() 传入 BaseUrl 进行构建
     */
    val retrofitNoRedirectBuilder: Retrofit.Builder by lazy {
        retrofitBuilder.client(
            okhttp.newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build()
        )
    }

    /**
     * 此 Retrofit 没有 Cookie 持久化
     */
    val retrofitNoPersistentBuilder: Retrofit.Builder by lazy {
        retrofitBuilder.client(
            okhttp.newBuilder()
                .cookieJar(CookieJar.NO_COOKIES)
                .build()
        )
    }


    /**
     * 加载一个插件
     *
     * @param apkPath 插件路径，需要设置为只读才可以
     */
    suspend fun loadExtension(apkPath: String, force: Boolean = false): List<MediaSourceExtension> {
        return withContext(Dispatchers.IO) {
            val key = File(apkPath).md5()

            // 清理缓存，强制重新加载
            if (force && cacheMap.contains(key)) {
                cacheMap.remove(key)
            }

            // 载入插件
            val extensions = cacheMap.getOrPut(key) {
                // 读取类
                val byteArrays: List<ByteArray> =
                    ZipInputStream(FileInputStream(apkPath)).use { zip ->
                        val dexList = mutableListOf<ByteArray>()
                        var entry: ZipEntry?
                        while ((zip.nextEntry.also { entry = it }) != null) {
                            val entryName = requireNotNull(entry).name.orEmpty()
                            if (entryName.endsWith(".dex")) {
                                val output = ByteArrayOutputStream()
                                val buffer = ByteArray(1024)
                                var len: Int
                                while (zip.read(buffer).also { len = it } > 0) {
                                    output.write(buffer, 0, len)
                                }
                                dexList.add(output.toByteArray())
                            }
                            zip.closeEntry()
                        }
                        dexList
                    }

                // 返回插件包的数据源集合
                byteArrays.flatMap {
                    val dexFile = DexBackedDexFile(Opcodes.getDefault(), it, 0)
                    val sources = arrayListOf<MediaSourceExtension>()

                    dexFile.classes.forEach { classDef ->
                        val annotations = classDef.annotations
                        val mediaSourceAnnotation = annotations.find { annotation ->
                            annotation.type == MediaSource::class.jvmSignature
                        }

                        if (mediaSourceAnnotation != null) {
                            val infoMap = mediaSourceAnnotation.elements.associate { element ->
                                element.name to element.value.toString()
                                    .trim { char -> char == '\"' }
                            }

                            val sourceInfo = MediaSourceInfo.loadFromMap(infoMap)
                            val className = classDef.type
                                .replace("L", "")
                                .replace("/", ".")
                                .replace(";", "")

                            val pathClassLoader = PathClassLoader(apkPath, parentClassLoader)
                            val sourceClass = pathClassLoader.loadClass(className)

                            val source = sourceClass.constructors.first().let { constructor ->
                                constructor.isAccessible = true
                                constructor.newInstance() as Source
                            }

                            sources.add(
                                MediaSourceExtension(
                                    info = sourceInfo,
                                    sourceClass = className,
                                    source = source,
                                    classLoader = pathClassLoader
                                )
                            )
                        }
                    }
                    sources
                }
            }

            // 加载源的 DNS
            extensions
                .mapNotNull { if (it.source is HttpSource) it.source else null }
                .forEach {
                    NetworkManager.putAll(it.dnsMap)
                }

            extensions
        }
    }

    /**
     * 清理
     */
    fun clean(apkPath: String) {
        if (cacheMap.contains(apkPath)) {
            cacheMap.remove(apkPath)
        }
    }

    /**
     * 清理全部
     */
    fun cleanAll() {
        cacheMap.clear()
    }
}