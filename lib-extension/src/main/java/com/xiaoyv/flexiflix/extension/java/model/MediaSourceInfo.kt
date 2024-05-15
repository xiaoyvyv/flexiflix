package com.xiaoyv.flexiflix.extension.java.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * [MediaSourceInfo] map to [com.xiaoyv.flexiflix.extension.annotation.MediaSource]
 *
 * @author why
 * @since 5/8/24
 */
@Parcelize
data class MediaSourceInfo(

    /**
     * 数据源唯一ID
     */
    val id: String,

    /**
     * 数据源名称
     */
    val name: String,

    /**
     * 数据源描述
     */
    val description: String,

    /**
     * 数据源作者
     */
    val author: String,

    /**
     * 成人内容
     */
    val nsfw: Boolean,

    /**
     * 数据源版本代码
     */
    val versionCode: Int,

    /**
     * 数据源版本名称
     */
    val versionName: String
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
