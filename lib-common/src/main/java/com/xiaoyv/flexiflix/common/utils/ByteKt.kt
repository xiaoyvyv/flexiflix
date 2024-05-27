package com.xiaoyv.flexiflix.common.utils

import kotlin.math.log10
import kotlin.math.pow

/**
 * [formatFileSize]
 *
 * @author why
 * @since 4/29/24
 */
fun Long.formatFileSize(): String {
    if (this <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(toDouble()) / log10(1024.0)).toInt()
    return "%.1f %s".format(this / 1024.0.pow(digitGroups.toDouble()), units[digitGroups])
}
