package com.xiaoyv.flexiflix.extension

import com.xiaoyv.flexiflix.extension.model.MediaSourceInfo
import com.xiaoyv.flexiflix.extension.source.Source

/**
 * [MediaSourceExtension]
 *
 * @author why
 * @since 5/8/24
 */
data class MediaSourceExtension(
    @MediaSourceType
    val type: Int = MediaSourceType.TYPE_UNKNOWN,
    val info: MediaSourceInfo,
    val source: Source,
    val classLoader: ClassLoader = ClassLoader.getSystemClassLoader()
)
