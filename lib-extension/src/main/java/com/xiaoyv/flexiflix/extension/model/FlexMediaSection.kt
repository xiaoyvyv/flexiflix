package com.xiaoyv.flexiflix.extension.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * [FlexMediaSection]
 *
 * @author why
 * @since 5/8/24
 */
@Parcelize
data class FlexMediaSection(
    @SerializedName("id") var id: String,
    @SerializedName("title") var title: String,
    @SerializedName("items") var items: List<FlexMediaSectionItem>,
    @SerializedName("extras") var extras: HashMap<String, String>? = hashMapOf()
) : Parcelable





