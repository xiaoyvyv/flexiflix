package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.extension

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * [addExtensionCompatScreen]
 *
 * @author why
 * @since 5/8/24
 */
private const val ROUTE_EXTENSION_COMPAT = "extension-compat"

fun NavController.navigateExtensionCompat() {
    navigate(ROUTE_EXTENSION_COMPAT)
}

fun NavGraphBuilder.addExtensionCompatScreen(navController: NavController) {
    composable(route = ROUTE_EXTENSION_COMPAT) {
        ExtensionCompatRoute(onNavUp = { navController.popBackStack() })
    }
}
