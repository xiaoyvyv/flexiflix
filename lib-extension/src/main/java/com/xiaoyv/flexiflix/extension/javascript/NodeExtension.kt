package com.xiaoyv.flexiflix.extension.javascript

/**
 * [NodeExtension]
 *
 * @author why
 * @since 5/13/24
 */
object NodeExtension {

    init {
        NodeNativeContext.init()
    }

    external fun stringFromJNI(): String

    external fun startNodeWithArguments(array: Array<String>): Int
}