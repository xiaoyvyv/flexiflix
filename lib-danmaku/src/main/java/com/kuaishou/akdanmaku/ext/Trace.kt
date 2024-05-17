/*
 * The MIT License (MIT)
 *
 * Copyright 2021 Kwai, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
@file:SuppressLint("UnclosedTrace")

package com.kuaishou.akdanmaku.ext

import android.annotation.SuppressLint
import android.os.Trace
import com.kuaishou.akdanmaku.BuildConfig

/**
 * Trace 相关封装
 *
 * @author Xana
 * @since 2021-07-08
 */
inline fun <T> withTrace(name: String, crossinline block: () -> T): T {
    if (!BuildConfig.DEBUG) return block()
    Trace.beginSection(name)
    val t = block()
    Trace.endSection()
    return t
}

fun startTrace(name: String) {
    if (!BuildConfig.DEBUG) return
    Trace.beginSection(name)
}

fun endTrace() {
    if (!BuildConfig.DEBUG) return
    Trace.endSection()
}
