package com.xiaoyv.flexiflix.extension.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * [FlexMediaDetailTab]
 *
 * @author why
 * @since 5/8/24
 */
@Parcelize
data class FlexMediaDetailTab(
    var id: String,
    var mediaId: String,
    var title: String,
    var extras: HashMap<String, String> = hashMapOf()
) : Parcelable
