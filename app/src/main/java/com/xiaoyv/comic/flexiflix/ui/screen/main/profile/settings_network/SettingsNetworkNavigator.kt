package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_network

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.about.navigateAbout

/**
 * [addSettingNetworkScreen]
 *
 * @author why
 * @since 5/8/24
 */
private const val ROUTE_SETTINGS_NETWORK = "settings-network"

fun NavController.navigateSettingNetwork() {
    navigate(ROUTE_SETTINGS_NETWORK)
}

fun NavGraphBuilder.addSettingNetworkScreen(navController: NavController) {
    composable(route = ROUTE_SETTINGS_NETWORK) {
        SettingNetworkRoute(
            onNavUp = { navController.popBackStack() },
            onNavAboutScreen = { navController.navigateAbout() }
        )
    }
}
