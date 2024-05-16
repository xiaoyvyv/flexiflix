package com.xiaoyv.flexiflix.extension

import androidx.annotation.IntDef

/**
 * [MediaSourceType]
 *
 * @author why
 * @since 5/15/24
 */
@IntDef(
    MediaSourceType.TYPE_UNKNOWN,
    MediaSourceType.TYPE_JVM,
    MediaSourceType.TYPE_PYTHON,
    MediaSourceType.TYPE_NODEJS
)
@Retention(AnnotationRetention.SOURCE)
annotation class MediaSourceType {
    companion object {
        const val TYPE_UNKNOWN = 0

        /**
         * JVM 插件
         */
        const val TYPE_JVM = 1

        /**
         * JavaScript 插件
         */
        const val TYPE_NODEJS = 2

        /**
         * Python 插件
         */
        const val TYPE_PYTHON = 3
    }
}
