package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.about.navigateAbout
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_beta.navigateSettingBeta
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_network.navigateSettingNetwork
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_player.navigateSettingPlayer
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_theme.navigateSettingTheme

/**
 * [addSettingsScreen]
 *
 * @author why
 * @since 5/8/24
 */
private const val ROUTE_SETTINGS = "settings"

fun NavController.navigateSettings() {
    navigate(ROUTE_SETTINGS)
}

fun NavGraphBuilder.addSettingsScreen(navController: NavController) {
    composable(route = ROUTE_SETTINGS) {
        SettingsRoute(
            onNavUp = { navController.popBackStack() },
            onNavNetworkScreen = { navController.navigateSettingNetwork() },
            onNavPlayerScreen = { navController.navigateSettingPlayer() },
            onNavThemeScreen = { navController.navigateSettingTheme() },
            onNavBetaScreen = { navController.navigateSettingBeta() },
            onNavAboutScreen = { navController.navigateAbout() }
        )
    }
}
