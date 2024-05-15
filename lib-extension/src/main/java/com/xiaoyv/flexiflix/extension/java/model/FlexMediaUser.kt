package com.xiaoyv.flexiflix.extension.java.model

import android.os.Parcelable
import com.xiaoyv.flexiflix.extension.java.utils.UNKNOWN_STRING
import kotlinx.parcelize.Parcelize

/**
 * [FlexMediaUser]
 *
 * @author why
 * @since 5/8/24
 */
@Parcelize
data class FlexMediaUser(
    var id: String = UNKNOWN_STRING,
    var name: String = UNKNOWN_STRING,
    var avatar: String = UNKNOWN_STRING,
    var role: String = UNKNOWN_STRING
) : Parcelable
