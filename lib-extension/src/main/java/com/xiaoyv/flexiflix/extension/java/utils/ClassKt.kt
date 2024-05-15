package com.xiaoyv.flexiflix.extension.java.utils

import kotlin.reflect.KClass

/**
 * [jvmSignature]
 *
 * @author why
 * @since 5/8/24
 */
val KClass<*>.jvmSignature
    get() = "L" + this.java.name.replace(".", "/") + ";"