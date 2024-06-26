package com.xiaoyv.flexiflix.extension.utils

import java.net.URLDecoder
import java.net.URLEncoder

fun String?.parseNumber(): Int {
    val orNull = this?.toIntOrNull()
    if (orNull != null) return orNull
    return "(\\d+)".toRegex().find(this ?: return 0)?.groupValues?.getOrNull(1)?.toIntOrNull() ?: 0
}

fun String?.parseNumberStr(): String {
    return parseNumber().toString()
}

/**
 * 使用正则表达式匹配 \u 开头的 Unicode 转义序列
 */
fun String?.decodeUnicode(): String {
    val regex = Regex("\\\\u([0-9A-Fa-f]{4})")
    return regex.replace(orEmpty()) { matchResult ->
        val unicodeChar = matchResult.groupValues[1].toInt(16).toChar()
        unicodeChar.toString()
    }
}

fun String?.decodeUrl(): String {
    return runCatchingPrint { URLDecoder.decode(orEmpty(), "UTF-8") }
        .getOrElse { this }
        .orEmpty()
}

fun String?.encodeUrl(): String {
    return runCatchingPrint { URLEncoder.encode(orEmpty(), "UTF-8") }
        .getOrElse { this }
        .orEmpty()
}
