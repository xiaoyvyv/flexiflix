package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.about

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * [addAboutScreen]
 *
 * @author why
 * @since 5/8/24
 */
private const val ROUTE_ABOUT = "about"

fun NavController.navigateAbout() {
    navigate(ROUTE_ABOUT)
}

fun NavGraphBuilder.addAboutScreen(navController: NavController) {
    composable(route = ROUTE_ABOUT) {
        AboutRoute(
            onNavUp = { navController.popBackStack() },
        )
    }
}
