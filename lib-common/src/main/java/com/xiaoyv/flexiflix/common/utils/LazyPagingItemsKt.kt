package com.xiaoyv.flexiflix.common.utils

import androidx.paging.compose.LazyPagingItems

/**
 * [first]
 *
 * @author why
 * @since 5/9/24
 */
fun <T : Any> LazyPagingItems<T>.first(): T {
    require(itemCount > 0)
    return requireNotNull(this[0])
}

fun <T : Any> LazyPagingItems<T>.last(): T {
    require(itemCount > 0)
    return requireNotNull(this[itemCount - 1])
}