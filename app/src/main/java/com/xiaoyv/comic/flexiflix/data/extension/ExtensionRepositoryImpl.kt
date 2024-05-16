package com.xiaoyv.comic.flexiflix.data.extension

import android.content.Context
import android.net.Uri
import com.xiaoyv.comic.flexiflix.model.InstalledMediaSource
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.common.utils.displayName
import com.xiaoyv.flexiflix.common.utils.formatTime
import com.xiaoyv.flexiflix.common.utils.inputStream
import com.xiaoyv.flexiflix.common.utils.realFilePath
import com.xiaoyv.flexiflix.extension.MediaSourceExtension
import com.xiaoyv.flexiflix.extension.MediaSourceFactory
import com.xiaoyv.flexiflix.extension.MediaSourceType
import com.xiaoyv.flexiflix.extension.source.Source
import com.xiaoyv.flexiflix.extension.utils.runCatchingPrint
import com.xiaoyv.flexiflix.extension.utils.toJson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ConcurrentHashMap
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
     * JVM 插件安装路径
     */
    private val extensionJvmInstallDir by lazy {
        File(context.filesDir, "extension/jvm").apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * JavaScript 插件安装路径
     */
    private val extensionJavaScriptInstallDir by lazy {
        File(context.filesDir, "extension/javascript").apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * Python 插件安装路径
     */
    private val extensionPythonInstallDir by lazy {
        File(context.filesDir, "extension/python").apply {
            if (!exists()) mkdirs()
        }
    }


    override suspend fun getInstalledExtensions(): Result<List<InstalledMediaSource>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                val jvmExtension = getInstalledExtensionsByType(MediaSourceType.TYPE_JVM)
                val javaScriptExtension = getInstalledExtensionsByType(MediaSourceType.TYPE_NODEJS)
                val pythonExtension = getInstalledExtensionsByType(MediaSourceType.TYPE_PYTHON)

                mutableListOf<InstalledMediaSource>().apply {
                    addAll(jvmExtension)
                    addAll(javaScriptExtension)
                    addAll(pythonExtension)
                }
            }
        }
    }

    /**
     * 根据类型加载安装的全部插件
     */
    private suspend fun getInstalledExtensionsByType(@MediaSourceType type: Int): List<InstalledMediaSource> {
        val dir = when (type) {
            MediaSourceType.TYPE_JVM -> extensionJvmInstallDir
            MediaSourceType.TYPE_NODEJS -> extensionJavaScriptInstallDir
            MediaSourceType.TYPE_PYTHON -> extensionPythonInstallDir
            else -> return emptyList()
        }
        val fileExt = when (type) {
            MediaSourceType.TYPE_JVM -> "apk"
            MediaSourceType.TYPE_NODEJS -> "js"
            MediaSourceType.TYPE_PYTHON -> "py"
            else -> return emptyList()
        }

        // 根据文件加载插件
        val sourceList = dir
            .walk()
            .filter { file -> file.isFile && file.absolutePath.endsWith(fileExt, true) }
            .toList()
            .mapNotNull {
                runCatchingPrint { it.toInstalledMediaSource(context, type) }.getOrNull()
            }

        // Map<SourceId,Source>
        val sourceMap = sourceList
            .flatMap { it.sources.map { extension -> extension.info.id to extension.source } }
            .associate { it }

        // 缓存
        sourceMapCache.putAll(sourceMap)

        return sourceList
    }

    override suspend fun installExtension(extensionUri: Uri): Result<InstalledMediaSource> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                val mediaSourceType = MediaSourceType.fromPath(extensionUri.realFilePath(context))
                val installDir = when (mediaSourceType) {
                    MediaSourceType.TYPE_JVM -> extensionJvmInstallDir
                    MediaSourceType.TYPE_NODEJS -> extensionJavaScriptInstallDir
                    MediaSourceType.TYPE_PYTHON -> extensionPythonInstallDir
                    else -> error("Not support!")
                }

                val installed = File(installDir, extensionUri.displayName(context))
                if (installed.exists()) {
                    installed.setWritable(true)
                    installed.delete()
                }

                // 复制到安装目录
                extensionUri.inputStream(context).use {
                    FileOutputStream(installed).use { out -> it.copyTo(out) }
                }

                // 判断是否合法
                val mediaSource = installed.toInstalledMediaSource(
                    context = context,
                    type = mediaSourceType
                )

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
    private suspend fun File.toInstalledMediaSource(
        context: Context,
        @MediaSourceType type: Int
    ): InstalledMediaSource? {
        when (type) {
            // JVM 插件
            MediaSourceType.TYPE_JVM -> {
                // 设置只读
                setReadOnly()

                // 加载插件
                val extensions = MediaSourceFactory.loadJvmExtension(absolutePath, true)
                if (extensions.isEmpty()) return null

                val manager = context.packageManager
                val packageInfo = manager.getPackageArchiveInfo(absolutePath, 0)
                val drawable = packageInfo?.applicationInfo?.loadIcon(manager)
                val extensionName = packageInfo?.applicationInfo?.loadLabel(manager)?.toString()
                    ?: nameWithoutExtension

                return InstalledMediaSource(
                    extensionName = extensionName,
                    extensionPath = absolutePath,
                    extensionIcon = drawable,
                    created = lastModified().formatTime("yyyy-MM-dd"),
                    sources = extensions
                )
            }

            MediaSourceType.TYPE_NODEJS -> {
                // 设置只读
                setReadOnly()

                // 加载插件
                val extensions = MediaSourceFactory.loadJavaScriptExtension(absolutePath, true)
                if (extensions.isEmpty()) return null

                return InstalledMediaSource(
                    extensionName = extensions.first().info.name,
                    extensionPath = absolutePath,
                    extensionIcon = null,
                    created = lastModified().formatTime("yyyy-MM-dd"),
                    sources = extensions
                )
            }

            MediaSourceType.TYPE_PYTHON -> {

            }

            MediaSourceType.TYPE_UNKNOWN -> {
                error("Not support for $this!")
            }
        }

        error("Not support for $this!")
    }
}