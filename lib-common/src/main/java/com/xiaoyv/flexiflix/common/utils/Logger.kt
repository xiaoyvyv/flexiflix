package com.xiaoyv.flexiflix.common.utils

import android.util.Log

/**
 * Class: [debugLog]
 *
 * @author why
 * @since 3/4/24
 */
inline fun debugLog(
    tag: String = "ComicLogger",
    location: Boolean = false,
    message: () -> Any? = { "" }
) {
    if (location) {
        val stackTrace = Thread.currentThread().stackTrace
        if (stackTrace.size > 2) {
            val caller = stackTrace[2]
            val lineNumber = caller.lineNumber
            val className = caller.className.substringAfterLast('.')
            val methodName = caller.methodName
            Log.e(tag, message().toString() + " - [$className.$methodName(:$lineNumber)]")
            return
        }
    }
    Log.e(tag, message().toString())
}