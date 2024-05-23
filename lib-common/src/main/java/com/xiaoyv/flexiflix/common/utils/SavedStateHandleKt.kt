package com.xiaoyv.flexiflix.common.utils

import android.util.Base64
import androidx.lifecycle.SavedStateHandle
import com.google.gson.reflect.TypeToken
import com.xiaoyv.flexiflix.extension.utils.gson
import com.xiaoyv.flexiflix.extension.utils.runCatchingPrint
import com.xiaoyv.flexiflix.extension.utils.toJson

/**
 * @author why
 * @since 5/20/24
 */
inline fun <reified T> SavedStateHandle.decodeBase64Obj(key: String): T {
    val json = Base64.decode(get<String>(key).orEmpty(), Base64.URL_SAFE).decodeToString()
    val type = object : TypeToken<T>() {}.type
    return gson.fromJson(json, type)
}

inline fun <reified T> SavedStateHandle.decodeBase64ObjSafe(key: String): T? {
    return runCatchingPrint { decodeBase64Obj<T>(key) }.getOrNull()
}


fun Any?.toBase64Obj(): String {
    this ?: return ""
    return Base64.encode(toJson().toByteArray(), Base64.URL_SAFE).decodeToString()
}