package com.xiaoyv.flexiflix.extension.java

import android.os.Parcelable
import com.xiaoyv.flexiflix.extension.java.model.MediaSourceInfo
import com.xiaoyv.flexiflix.extension.java.source.Source
import dalvik.system.PathClassLoader
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * [MediaSourceExtension]
 *
 * @author why
 * @since 5/8/24
 */
data class MediaSourceExtension(
    val info: MediaSourceInfo,
    val sourceClass: String,
    val source: Source,
    val classLoader: PathClassLoader
)
