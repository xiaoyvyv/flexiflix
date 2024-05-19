package com.xiaoyv.flexiflix.extension.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 搜索的关键字的 key 和可选的过滤项目
 */
@Parcelize
data class FlexSearchOption(
    @SerializedName("keywordKey") var keywordKey: String,
    @SerializedName("options") var options: List<FlexSearchOptionItem>? = null
) : Parcelable

