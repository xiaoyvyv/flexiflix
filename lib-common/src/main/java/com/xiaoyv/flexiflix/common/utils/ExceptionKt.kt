package com.xiaoyv.flexiflix.common.utils

import com.xiaoyv.flexiflix.extension.utils.gson
import retrofit2.HttpException

/**
 * [Exception]
 *
 * @author why
 * @since 5/18/24
 */
val Throwable?.errMsg: String
    get() {
        val exception = this ?: return "发送错误了"
        if (exception is HttpException) {
            val empty = exception.response()?.errorBody()?.string().orEmpty()
            runCatching {
                val error = gson.fromJson(empty, Map::class.java)
                    .getOrDefault("error", "")
                    .toString()
                if (error.isNotBlank()) {
                    return error
                }
            }
        }

        return exception.message.orEmpty()
    }