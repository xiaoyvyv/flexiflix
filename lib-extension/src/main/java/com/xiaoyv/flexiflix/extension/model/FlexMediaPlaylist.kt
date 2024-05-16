package com.xiaoyv.flexiflix.extension.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * [FlexMediaPlaylist]
 *
 * @author why
 * @since 5/8/24
 */
@Parcelize
data class FlexMediaPlaylist(
    var title: String = "默认",
    var items: List<FlexMediaPlaylistUrl> = emptyList()
) : Parcelable
