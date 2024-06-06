package com.xiaoyv.flexiflix.common.utils

import androidx.navigation.NavController

/**
 * [navigateByPath]
 *
 * @author why
 * @since 5/9/24
 */
fun NavController.navigateByPath(
    route: String,
    params: List<String> = emptyList(),
    optional: Map<String, String> = emptyMap(),
) {
    val hasEmpty = params.find { it.isBlank() } != null
    if (hasEmpty) {
        debugLog {
            "Navigate to $route fail! params can't has empty element -> [${params.joinToString(",")}]"
        }
        return
    }

    val path = params.joinToString("/")
    val query = optional.entries.joinToString("&") { it.key + "=" + it.value }

    if (params.isNotEmpty()) {
        navigate("$route/$path?$query")
    } else {
        navigate("$route?$query")
    }
}
