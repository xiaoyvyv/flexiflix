package com.xiaoyv.comic.flexiflix.model

import com.xiaoyv.flexiflix.extension.MediaSourceExtension

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
    val extensionIcon: Any? = null
)