package com.xiaoyv.flexiflix.extension.utils

import com.google.gson.Gson

/**
 * [toJson]
 *
 * @author why
 * @since 5/4/24
 */
val gson: Gson by lazy { Gson().newBuilder().create() }
val gsonPretty: Gson by lazy { gson.newBuilder().setPrettyPrinting().create() }

fun Any?.toJson(pretty: Boolean = false): String {
    return if (pretty) gsonPretty.toJson(this)
    else gson.toJson(this)
}