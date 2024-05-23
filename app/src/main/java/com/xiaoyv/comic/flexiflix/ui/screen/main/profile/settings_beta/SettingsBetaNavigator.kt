package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_beta

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.about.navigateAbout

/**
 * [addSettingBetaScreen]
 *
 * @author why
 * @since 5/8/24
 */
private const val ROUTE_SETTINGS_BETA = "settings-beta"

fun NavController.navigateSettingBeta() {
    navigate(ROUTE_SETTINGS_BETA)
}

fun NavGraphBuilder.addSettingBetaScreen(navController: NavController) {
    composable(route = ROUTE_SETTINGS_BETA) {
        SettingBetaRoute(
            onNavUp = { navController.popBackStack() },
            onNavAboutScreen = { navController.navigateAbout() }
        )
    }
}
