package com.xiaoyv.flexiflix.extension.java.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * [FlexMediaDetailSeries]
 *
 * @author why
 * @since 5/8/24
 */
@Parcelize
data class FlexMediaDetailSeries(
    var title: String = "默认",
    var mediaId: String,
    var count: Int = 0,
    var items: List<FlexMediaSectionItem> = emptyList()
) : Parcelable
