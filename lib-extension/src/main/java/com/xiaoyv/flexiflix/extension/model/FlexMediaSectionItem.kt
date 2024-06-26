package com.xiaoyv.flexiflix.extension.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * [FlexMediaSectionItem]
 *
 * @author why
 * @since 5/8/24
 */
@Parcelize
data class FlexMediaSectionItem(
    @SerializedName("id") var id: String,
    @SerializedName("title") var title: String,
    @SerializedName("cover") var cover: String,
    @SerializedName("description") var description: String?,
    @SerializedName("user") var user: FlexMediaUser? = null,
    @SerializedName("extras") var extras: HashMap<String, String>? = hashMapOf(),
    @SerializedName("overlay") var overlay: OverlayText? = null,
    @SerializedName("layout") var layout: ImageLayout? = null,
) : Parcelable {
    val requireOverlay
        get() = overlay ?: OverlayText()

    val requireLayout
        get() = layout ?: ImageLayout()

    @Parcelize
    data class ImageLayout(
        @SerializedName("width") var widthDp: Int = 185,
        @SerializedName("aspectRatio") var aspectRatio: Float = 16 / 9f,
    ) : Parcelable

    @Parcelize
    data class OverlayText(
        var topStart: String? = "",
        var topEnd: String? = "",
        var bottomStart: String? = "",
        var bottomEnd: String? = "",
    ) : Parcelable
}