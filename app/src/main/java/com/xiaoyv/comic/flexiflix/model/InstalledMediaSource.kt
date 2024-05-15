package com.xiaoyv.comic.flexiflix.model

import android.graphics.drawable.Drawable
import android.os.Parcelable
import com.xiaoyv.flexiflix.extension.java.MediaSourceExtension
import com.xiaoyv.flexiflix.extension.java.model.MediaSourceInfo
import kotlinx.parcelize.Parcelize

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
    val extensionIcon: Drawable? = null
)