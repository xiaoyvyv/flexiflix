package com.xiaoyv.flexiflix.extension.java.utils

import android.util.Log
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.intellij.lang.annotations.RegExp
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * 获取 href 链接的某个参数
 */
fun Element.hrefParam(parameter: String, default: String = UNKNOWN_STRING): String {
    return attrUrlParam("href", parameter, default)
}

/**
 * 获取 href 链接的某个参数
 */
fun Elements.hrefParam(parameter: String, default: String = UNKNOWN_STRING): String {
    return attrUrlParam("href", parameter, default)

}

/**
 * 获取 src 链接的某个参数
 */
fun Element.srcParam(parameter: String, default: String = UNKNOWN_STRING): String {
    return attrUrlParam("src", parameter, default)
}

/**
 * 获取 src 链接的某个参数
 */
fun Elements.srcParam(parameter: String, default: String = UNKNOWN_STRING): String {
    return attrUrlParam("src", parameter, default)
}

/**
 * 获取指定链接类型的 attr 的某个参数
 */
fun Element.attrUrlParam(
    attr: String,
    parameter: String,
    default: String = UNKNOWN_STRING
): String {
    val httpUrl = attr(attr).toHttpUrlOrNull() ?: return default
    return httpUrl.queryParameter(parameter) ?: default
}

/**
 * 获取 src 链接的某个参数
 */
fun Elements.attrUrlParam(
    attr: String,
    parameter: String,
    default: String = UNKNOWN_STRING
): String {
    val httpUrl = attr(attr).toHttpUrlOrNull() ?: return default
    return httpUrl.queryParameter(parameter) ?: default
}

fun Elements.childText(index: Int, default: String = UNKNOWN_STRING): String {
    return getOrNull(index)?.text().orEmpty().trim().ifBlank { default }
}


/**
 * 通过 `property` 提取  `<meta> content`
 *
 * 示例
 *
 * ```kotlin
 * Document.metaProperty(property="og:url")
 * ```
 * ```html
 * <meta property="og:url" content="https://hanime1.me/watch?v=93929" />
 * ```
 */
fun Document.metaContentByProperty(property: String): String {
    return select("meta[property=$property]").firstOrNull()?.attr("content").orEmpty()
}

/**
 * 通过 `name` 提取 `<meta> content`
 *
 * 示例
 *
 * ```kotlin
 * Document.metaProperty(property="og:url")
 * ```
 * ```html
 * <meta name="cover" content="https://hanime1.me/watch?v=93929" />
 * ```
 */
fun Document.metaContentByName(name: String): String {
    return select("meta[name=$name]").firstOrNull()?.attr("content").orEmpty()
}

/**
 * 全文正则匹配
 */
fun Document.regex(select: String, @RegExp regex: String, group: Int = 0): String {
    val result = regex.toRegex().find(select(select).html())
    if (group == 0) return result?.value.orEmpty()
    val groupValues = result?.groupValues.orEmpty()
    return groupValues.getOrNull(group).orEmpty()
}

/**
 * 全文正则匹配
 */
fun Element?.regex(@RegExp regex: String, group: Int = 0): String {
    val result = regex.toRegex().find(this?.text().orEmpty() + "\n")
    if (group == 0) return result?.value.orEmpty()
    val groupValues = result?.groupValues.orEmpty()
    return groupValues.getOrNull(group).orEmpty()
}


