package com.xiaoyv.flexiflix.common.utils

import androidx.compose.runtime.MutableState
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * [glideLoadListener]
 *
 * @author why
 * @since 5/2/24
 */
inline fun <T : Any> glideLoadListener(
    crossinline onLoadFailed: (GlideException?) -> Boolean = { false },
    crossinline onResourceReady: (T) -> Boolean = { false },
): RequestListener<T> {
    return object : RequestListener<T> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<T>,
            isFirstResource: Boolean
        ): Boolean {
            return onLoadFailed(e)
        }

        override fun onResourceReady(
            resource: T,
            model: Any,
            target: Target<T>?,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            return onResourceReady(resource)
        }
    }
}

fun <T : Any> RequestBuilder<T>.addStateListener(loading: MutableState<Boolean>): RequestBuilder<T> {
    return addListener(glideLoadListener(
        onLoadFailed = {
            loading.value = false
            false
        },
        onResourceReady = {
            loading.value = false
            false
        }
    ))
}