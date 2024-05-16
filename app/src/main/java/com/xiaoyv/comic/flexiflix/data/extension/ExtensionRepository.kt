package com.xiaoyv.comic.flexiflix.data.extension

import android.net.Uri
import com.xiaoyv.comic.flexiflix.model.InstalledMediaSource
import com.xiaoyv.flexiflix.extension.MediaSourceExtension
import com.xiaoyv.flexiflix.extension.source.Source
import java.io.File

/**
 * [ExtensionRepository]
 *
 * @author why
 * @since 5/10/24
 */
interface ExtensionRepository {

    /**
     * 安装一个插件
     */
    suspend fun installExtension(extensionUri: Uri): Result<InstalledMediaSource>

    /**
     * 加载一个插件文件，可能有多个源
     */
    suspend fun loadExtensionFile(file: File): Result<List<MediaSourceExtension>>

    /**
     * 获取安装的全部插件
     */
    suspend fun getInstalledExtensions(): Result<List<InstalledMediaSource>>

    /**
     * 根据源ID获取对应的源
     */
    suspend fun getExtensionById(sourceId: String): Result<Source>
}