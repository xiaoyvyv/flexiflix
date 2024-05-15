package com.xiaoyv.flexiflix.common.utils

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * [mutableStateFlowOf]
 */
inline fun <reified T> mutableStateFlowOf(v: T) = MutableStateFlow(value = v)