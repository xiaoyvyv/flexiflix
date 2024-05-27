package com.xiaoyv.comic.flexiflix.model

import com.xiaoyv.flexiflix.extension.MediaSourceExtension
import com.xiaoyv.flexiflix.extension.MediaSourceType

/**
 * [InstalledMediaSource]
 *
 * @author why
 * @since 5/10/24
 */
data class InstalledMediaSource(
    val created: String,
    val sources: List<MediaSourceExtension>,
    val extensionName: String,
    val extensionPath: String,
    val extensionIcon: Any? = null,

    /**
     * 插件类型
     */
    @MediaSourceType
    val type: Int = MediaSourceType.TYPE_UNKNOWN,
) {
    /**
     * 是否有 NSFW 内容
     */
    val hasNsfw by lazy { sources.find { it.info.nsfw } != null }
}