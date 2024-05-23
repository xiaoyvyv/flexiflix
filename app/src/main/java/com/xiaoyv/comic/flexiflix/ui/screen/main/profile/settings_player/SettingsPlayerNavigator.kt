package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_player

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.about.navigateAbout

/**
 * [addSettingPlayerScreen]
 *
 * @author why
 * @since 5/8/24
 */
private const val ROUTE_SETTINGS_PLAYER = "settings-player"

fun NavController.navigateSettingPlayer() {
    navigate(ROUTE_SETTINGS_PLAYER)
}

fun NavGraphBuilder.addSettingPlayerScreen(navController: NavController) {
    composable(route = ROUTE_SETTINGS_PLAYER) {
        SettingPlayerRoute(
            onNavUp = { navController.popBackStack() },
            onNavAboutScreen = { navController.navigateAbout() }
        )
    }
}
