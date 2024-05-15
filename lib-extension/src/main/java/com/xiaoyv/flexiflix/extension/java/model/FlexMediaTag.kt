package com.xiaoyv.flexiflix.extension.java.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * [FlexMediaTag]
 *
 * @author why
 * @since 5/8/24
 */
@Parcelize
data class FlexMediaTag(
    var id: String,
    var name: String
) : Parcelable
