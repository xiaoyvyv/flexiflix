package com.xiaoyv.flexiflix.common.model

/**
 * [StateContent]
 *
 * @author why
 * @since 5/9/24
 */
sealed interface StateContent<T> {
    class Idle<T : Any> : StateContent<T>
    data class Payload<T>(val data: T) : StateContent<T>
}

val StateContent<*>.hasData: Boolean
    get() {
        if (this is StateContent.Payload<*>) {
            val any = this.data
            if (any is List<*>?) {
                return any?.isNotEmpty() == true
            }
            return true
        }
        return false
    }

inline fun <reified T : Any> StateContent<T>.payload(): T {
    return (this as StateContent.Payload<T>).data
}

/**
 * 单页，非列表的页面，配合 PageStateScreen 判断是否有内容了
 */
fun <T> StateContent<T>.asSinglePage(): Int {
    return if (hasData) 1 else 0
}

