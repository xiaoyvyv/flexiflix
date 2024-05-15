package com.xiaoyv.comic.flexiflix.ui.screen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * [addMainTabScreen]
 *
 * @author why
 * @since 5/8/24
 */
const val ROUTE_MAIN_TAB = "main"

fun NavGraphBuilder.addMainTabScreen(navController: NavController) {
    composable(
        route = ROUTE_MAIN_TAB,
        arguments = listOf()
    ) {
        MainTabRoute(navController)
    }
}
