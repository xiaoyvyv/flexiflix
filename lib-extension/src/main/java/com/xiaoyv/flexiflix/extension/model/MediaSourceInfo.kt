package com.xiaoyv.flexiflix.extension.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * [MediaSourceInfo]
 *
 * @author why
 * @since 5/8/24
 */
@Parcelize
data class MediaSourceInfo(

    /**
     * 数据源唯一ID
     */
    @SerializedName("id") val id: String,

    /**
     * 数据源名称
     */
    @SerializedName("name") val name: String,

    /**
     * 数据源描述
     */
    @SerializedName("description") val description: String,

    /**
     * 数据源描述
     */
    @SerializedName("icon") val icon: String,

    /**
     * 数据源作者
     */
    @SerializedName("author") val author: String,

    /**
     * 成人内容
     */
    @SerializedName("nsfw") val nsfw: Boolean,

    /**
     * 数据源版本代码
     */
    @SerializedName("versionCode") val versionCode: Int,

    /**
     * 数据源版本名称
     */
    @SerializedName("versionName") val versionName: String
) : Parcelable {
    companion object {
        @JvmStatic
        fun loadFromMap(infoMap: Map<String, String>): MediaSourceInfo {
            return MediaSourceInfo(
                id = infoMap.getOrDefault(
                    key = MediaSourceInfo::id.name,
                    defaultValue = ""
                ),
                name = infoMap.getOrDefault(
                    key = MediaSourceInfo::name.name,
                    defaultValue = "名称不见了"
                ),
                description = infoMap.getOrDefault(
                    key = MediaSourceInfo::description.name,
                    defaultValue = "暂时没有描述"
                ),
                icon = infoMap.getOrDefault(
                    key = MediaSourceInfo::icon.name,
                    defaultValue = ""
                ),
                author = infoMap.getOrDefault(
                    key = MediaSourceInfo::author.name,
                    defaultValue = "未知"
                ),
                nsfw = infoMap.getOrDefault(
                    key = MediaSourceInfo::nsfw.name,
                    defaultValue = "false"
                ).toBoolean(),
                versionCode = infoMap.getOrDefault(
                    key = MediaSourceInfo::versionCode.name,
                    defaultValue = "1"
                ).toIntOrNull() ?: 1,
                versionName = infoMap.getOrDefault(
                    key = MediaSourceInfo::versionName.name,
                    defaultValue = "v1.0.0"
                )
            )
        }
    }
}
