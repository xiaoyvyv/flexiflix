package com.xiaoyv.flexiflix.extension.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
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
    /**
     * 播放列表ID 同一个媒体条目内，该值必须唯一
     *
     * 你的数据源播放列表没有 ID 也要自己生成一个假的唯一值
     */
    @SerializedName("id") var id: String,

    /**
     * 标题
     */
    @SerializedName("title") var title: String,

    /**
     * 媒体真实链接源，可以有多个不同的分辨率
     */
    @SerializedName("mediaUrls") var mediaUrls: List<SourceUrl>? = emptyList(),

    /**
     * 封面
     */
    @SerializedName("cover") var cover: String? = UNKNOWN_STRING,
) : Parcelable {

    /**
     * 媒体真实链接源
     */
    @Parcelize
    data class SourceUrl(
        @SerializedName("name") var name: String,
        @SerializedName("rawUrl") var rawUrl: String,
        @SerializedName("size") var size: String? = null,
        @SerializedName("type") var type: String? = null,
    ) : Parcelable

    /**
     * 如果 URL 为空，则需要通过 [FlexMediaPlaylistUrl.id] 去查询媒体真实链接
     */
    val needLoadRawUrlById: Boolean
        get() = mediaUrls.isNullOrEmpty()
}
