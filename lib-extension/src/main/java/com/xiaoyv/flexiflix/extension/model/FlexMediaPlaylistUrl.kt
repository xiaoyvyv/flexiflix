package com.xiaoyv.flexiflix.extension.model

import android.os.Parcelable
import com.xiaoyv.flexiflix.extension.utils.UNKNOWN_STRING
import kotlinx.parcelize.Parcelize

/**
 * [FlexMediaPlaylistUrl]
 *
 * @author why
 * @since 5/8/24
 */
@Parcelize
data class FlexMediaPlaylistUrl(
    var id: String,
    var title: String,
    var mediaUrl: String?,
    var cover: String? = UNKNOWN_STRING,
    var type: String? = UNKNOWN_STRING,
    var size: String? = UNKNOWN_STRING
) : Parcelable {

    /**
     * 如果 URL 为空，则需要通过Source的 ID 去查询URL
     */
    val needLoadRawUrlById: Boolean
        get() = mediaUrl.isNullOrBlank()
}
