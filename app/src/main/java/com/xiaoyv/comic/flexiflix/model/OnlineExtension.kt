package com.xiaoyv.comic.flexiflix.model
import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 * [OnlineExtension]
 *
 * @author why
 * @since 5/24/24
 */
@Keep
@Parcelize
data class OnlineExtension(
    @SerializedName("author")
    var author: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("icon")
    var icon: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("nsfw")
    var nsfw: Boolean = false,
    @SerializedName("sourceCount")
    var sourceCount: Int = 0,
    @SerializedName("type")
    var type: Int = 0,
    @SerializedName("url")
    var url: String? = null,
    @SerializedName("versionCode")
    var versionCode: Int = 0,
    @SerializedName("versionName")
    var versionName: String? = null
) : Parcelable