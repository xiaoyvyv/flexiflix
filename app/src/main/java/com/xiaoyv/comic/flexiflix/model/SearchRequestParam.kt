package com.xiaoyv.comic.flexiflix.model

/**
 * [SearchRequestParam]
 *
 * @author why
 * @since 5/20/24
 */
data class SearchRequestParam(
    val sourceId: String,
    val keyword: String,
    val queryMap: Map<String, String>,
)
