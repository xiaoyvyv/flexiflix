package com.xiaoyv.flexiflix.extension.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * [FlexKeyValue]
 *
 * @author why
 * @since 5/18/24
 */
@Parcelize
data class FlexKeyValue(
    @SerializedName("key") val key: String,
    @SerializedName("value") val value: String
) : Parcelable
