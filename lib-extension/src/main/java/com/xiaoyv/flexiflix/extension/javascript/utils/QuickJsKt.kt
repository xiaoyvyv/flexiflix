package com.xiaoyv.flexiflix.extension.javascript.utils

import android.os.Handler
import com.whl.quickjs.wrapper.JSFunction
import com.whl.quickjs.wrapper.JSObject
import com.whl.quickjs.wrapper.QuickJSContext
import com.xiaoyv.flexiflix.extension.java.utils.gson
import kotlinx.coroutines.runBlocking

/**
 * [createPromise]
 *
 * @author why
 * @since 5/15/24
 */
inline fun QuickJSContext.createPromise(
    threadHandler: Handler,
    crossinline action: suspend () -> Any
): JSObject {
    val obj = createNewJSObject()
    obj.setProperty("action") {
        val resolve = it.paramAsMethod(0, true)
        val reject = it.paramAsMethod(1, true)

        threadHandler.post {
            try {
                resolve?.call(runBlocking { action.invoke() })
            } catch (e: Throwable) {
                reject?.call(e.message)
            }
        }

        true
    }

    val js = "createPromise: (action) => {\n" +
            "    return new Promise((resolve, reject) => {\n" +
            "        action(resolve, reject)\n" +
            "    });\n" +
            "}"
    val createPromise = evaluate(js) as JSFunction

    return createPromise.call(obj.getProperty("action")) as JSObject
}

fun Array<Any>.paramAsObj(index: Int, hold: Boolean = false): JSObject? {
    val jsObject = getOrNull(index) as? JSObject
    if (hold) jsObject?.hold()
    return jsObject
}

fun Array<Any>.paramAsMethod(index: Int, hold: Boolean = false): JSFunction? {
    val jsFunction = getOrNull(index) as? JSFunction
    if (hold) jsFunction?.hold()
    return jsFunction
}

fun Array<Any>.paramAsLong(index: Int, default: Long = 0): Long {
    return getOrNull(index)?.toString()?.toLongOrNull() ?: default
}

fun Array<Any>.paramAsInt(index: Int, default: Int = 0): Int {
    return getOrNull(index)?.toString()?.toIntOrNull() ?: default
}

fun Array<Any>.paramAsString(index: Int, default: String = ""): String {
    return getOrNull(index)?.toString() ?: default
}

fun JSObject?.string(name: String, default: String = ""): String {
    return this?.getProperty(name)?.toString() ?: default
}

fun JSObject?.jsMethod(name: String): JSFunction? {
    return this?.getJSFunction(name)
}

fun JSObject?.objMap(
    name: String,
    default: Map<String, Any> = emptyMap()
): Map<String, Any> {
    val jsObject = this?.getProperty(name) as? JSObject ?: return default
    return gson.fromJson<Map<String, Any>>(jsObject.stringify(), Map::class.java)
}