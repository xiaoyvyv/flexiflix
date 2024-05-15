package com.xiaoyv.flexiflix.common.utils

import androidx.navigation.NavController

/**
 * [navigateSafe]
 *
 * @author why
 * @since 5/9/24
 */
fun NavController.navigateSafe(route: String, vararg params: String) {
    val hasEmpty = params.find { it.isBlank() } != null
    if (hasEmpty) {
        debugLog {
            "Navigate to $route fail! params can't has empty element -> [${
                params.joinToString(",")
            }]"
        }
        return
    }

    navigate(route + "/" + params.joinToString("/"))
}