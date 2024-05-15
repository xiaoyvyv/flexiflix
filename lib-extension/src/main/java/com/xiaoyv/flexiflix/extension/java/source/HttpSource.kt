package com.xiaoyv.flexiflix.extension.java.source

import android.util.Log
import com.xiaoyv.flexiflix.extension.BuildConfig
import com.xiaoyv.flexiflix.extension.java.MediaSourceFactory
import com.xiaoyv.flexiflix.extension.java.utils.ignoreCertificate
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * [HttpSource]
 *
 * @author why
 * @since 5/8/24
 */
abstract class HttpSource : Source {
    abstract val baseUrl: String

    /**
     * 该数据源是否忽略SSL证书
     */
    open val ignoreCertificate: Boolean = false

    /**
     * 网络请求的 [OkHttpClient]
     */
    open val client: OkHttpClient by lazy {
        MediaSourceFactory.okhttp
            .newBuilder()
            .cookieJar(cookieJar)
            .let { if (ignoreCertificate) it.ignoreCertificate() else it }
            .build()
    }

    /**
     * Cookie 管理
     */
    open val cookieJar: CookieJar
        get() = MediaSourceFactory.cookieJar

    /**
     * Retrofit
     */
    open val retrofit: Retrofit by lazy {
        MediaSourceFactory.retrofitBuilder
            .baseUrl(baseUrl)
            .client(client)
            .build()
    }

    /**
     * 自定义 DNS 解析，自动添加到 client
     *
     * - key: 域名
     * - value: IP
     */
    open val dnsMap: Map<String, String>
        get() = emptyMap()

    /**
     * 日志打印
     */
    fun debugLog(any: () -> Any?) {
        if (BuildConfig.DEBUG) Log.e(javaClass.simpleName, any.invoke().toString())
    }
}