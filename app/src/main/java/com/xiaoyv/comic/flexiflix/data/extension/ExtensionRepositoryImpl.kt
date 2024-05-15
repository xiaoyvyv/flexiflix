package com.xiaoyv.comic.flexiflix.data.extension

import android.content.Context
import android.net.Uri
import com.xiaoyv.comic.flexiflix.model.InstalledMediaSource
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.common.utils.displayName
import com.xiaoyv.flexiflix.common.utils.formatTime
import com.xiaoyv.flexiflix.common.utils.inputStream
import com.xiaoyv.flexiflix.extension.java.MediaSourceExtension
import com.xiaoyv.flexiflix.extension.java.MediaSourceFactory
import com.xiaoyv.flexiflix.extension.java.source.Source
import com.xiaoyv.flexiflix.extension.java.utils.runCatchingPrint
import com.xiaoyv.flexiflix.extension.java.utils.toJson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

/**
 * [ExtensionRepositoryImpl]
 *
 * @author why
 * @since 5/10/24
 */
class ExtensionRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
) : ExtensionRepository {
    private val sourceMapCache = ConcurrentHashMap<String, Source>()

    /**
     * 插件安装路径
     */
    private val extensionInstallDir by lazy {
        File(context.filesDir, "extension").apply {
            if (!exists()) mkdirs()
        }
    }

    override suspend fun getInstalledExtensions(): Result<List<InstalledMediaSource>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                val sourceList = extensionInstallDir
                    .listFiles { file ->
                        file.isFile && file.absolutePath.endsWith("apk", true)
                    }
                    .apply { debugLog { this.toJson(true) } }
                    .orEmpty()
                    .mapNotNull {
                        it.toInstalledMediaSource(context)
                    }

                // Map<SourceId,Source>
                val sourceMap = sourceList
                    .flatMap { it.sources.map { extension -> extension.info.id to extension.source } }
                    .associate { it }

                // 缓存
                sourceMapCache.putAll(sourceMap)

                sourceList
            }
        }
    }

    override suspend fun installExtension(extensionUri: Uri): Result<InstalledMediaSource> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                val installed = File(extensionInstallDir, extensionUri.displayName(context))
                if (installed.exists()) {
                    installed.setWritable(true)
                    installed.delete()
                }

                // 复制到安装目录
                extensionUri.inputStream(context).use {
                    FileOutputStream(installed).use { out -> it.copyTo(out) }
                }

                // 判断是否合法
                val mediaSource = installed.toInstalledMediaSource(context)
                if (mediaSource == null || mediaSource.sources.isEmpty()) {
                    installed.setWritable(true)
                    installed.delete()
                    MediaSourceFactory.clean(installed.absolutePath)
                    error("不合法的插件源")
                }

                mediaSource
            }
        }
    }

    override suspend fun loadExtensionFile(file: File): Result<List<MediaSourceExtension>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                // 设置只读
                file.setReadOnly()

                // 加载插件
                MediaSourceFactory.loadExtension(file.absolutePath, true)
            }
        }
    }

    override suspend fun getExtensionById(sourceId: String): Result<Source> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                if (!sourceMapCache.contains(sourceId)) {
                    getInstalledExtensions()
                }

                requireNotNull(sourceMapCache[sourceId]) {
                    "Source not found which id = $sourceId!"
                }
            }
        }
    }

    /**
     * 读取文件，加载文件的全部插件
     */
    private suspend fun File.toInstalledMediaSource(context: Context): InstalledMediaSource? {
        val manager = context.packageManager
        val it = this
        val extensions: List<MediaSourceExtension> =
            loadExtensionFile(it).getOrNull() ?: return null
        val packageInfo = manager.getPackageArchiveInfo(it.absolutePath, 0)
        val drawable = packageInfo?.applicationInfo?.loadIcon(manager)
        val extensionName =
            packageInfo?.applicationInfo?.loadLabel(manager)?.toString()
                ?: it.nameWithoutExtension

        return InstalledMediaSource(
            extensionName = extensionName,
            extensionPath = it.absolutePath,
            extensionIcon = drawable,
            created = it.lastModified().formatTime("yyyy-MM-dd"),
            sources = extensions
        )
    }
}