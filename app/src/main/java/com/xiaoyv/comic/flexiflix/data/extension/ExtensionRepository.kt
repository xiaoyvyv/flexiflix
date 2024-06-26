package com.xiaoyv.comic.flexiflix.data.extension

import android.net.Uri
import com.xiaoyv.comic.flexiflix.model.InstalledMediaSource
import com.xiaoyv.comic.flexiflix.model.OnlineExtension
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
     * 获取在线插件列表
     */
    suspend fun getOnlineExtensions(): Result<List<OnlineExtension>>

    /**
     * 获取安装的全部插件
     */
    suspend fun getInstalledExtensions(): Result<List<InstalledMediaSource>>

    /**
     * 根据源ID获取对应的源
     */
    suspend fun getExtensionById(sourceId: String): Result<Source>
}