package com.xiaoyv.flexiflix.extension.java.utils

fun String?.parseNumber(): Int {
    val orNull = this?.toIntOrNull()
    if (orNull != null) return orNull
    return "(\\d+)".toRegex().find(this ?: return 0)?.groupValues?.getOrNull(1)?.toIntOrNull() ?: 0
}

fun String?.parseNumberStr(): String {
    return parseNumber().toString()
}

