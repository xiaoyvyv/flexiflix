package com.xiaoyv.flexiflix.extension.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.xiaoyv.flexiflix.extension.utils.UNKNOWN_STRING
import kotlinx.parcelize.Parcelize

/**
 * [FlexMediaDetail]
 *
 * @author why
 * @since 5/8/24
 */
@Parcelize
data class FlexMediaDetail(
    @SerializedName("id") var id: String,
    @SerializedName("title") var title: String,
    @SerializedName("description") var description: String,
    @SerializedName("cover") var cover: String,
    @SerializedName("url") var url: String,
    @SerializedName("type") var type: String? = UNKNOWN_STRING,
    @SerializedName("playCount") var playCount: String? = UNKNOWN_STRING,
    @SerializedName("createAt") var createAt: String? = UNKNOWN_STRING,
    @SerializedName("duration") var duration: Long = 0,
    @SerializedName("size") var size: String? = UNKNOWN_STRING,
    @SerializedName("publisher") var publisher: FlexMediaUser? = FlexMediaUser(),
    @SerializedName("playlist") var playlist: List<FlexMediaPlaylist>? = emptyList(),
    @SerializedName("series") var series: List<FlexMediaDetailSeries>? = emptyList(),
    @SerializedName("tags") var tags: List<FlexMediaTag>? = emptyList(),
    @SerializedName("relativeTabs") var relativeTabs: List<FlexMediaDetailTab>? = emptyList(),
    @SerializedName("extras") var extras: HashMap<String, String>? = hashMapOf()
) : Parcelable