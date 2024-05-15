package com.xiaoyv.flexiflix.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * [formatTime]
 *
 * @author why
 * @since 4/29/24
 */
fun Long.formatTime(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    runCatching {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(Date(this))
    }
    return ""
}