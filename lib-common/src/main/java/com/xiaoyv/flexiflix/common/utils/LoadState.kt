package com.xiaoyv.flexiflix.common.utils

import androidx.paging.LoadState

/**
 * [isStoped]
 *
 * @author why
 * @since 4/28/24
 */
val LoadState.isStoped: Boolean
    get() = this is LoadState.Error || this is LoadState.NotLoading

val LoadState.isError: Boolean
    get() = this is LoadState.Error

val LoadState.isNotLoading: Boolean
    get() = this is LoadState.NotLoading

val LoadState.isLoading: Boolean
    get() = this is LoadState.Loading

