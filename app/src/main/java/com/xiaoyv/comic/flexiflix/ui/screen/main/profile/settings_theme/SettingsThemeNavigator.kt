package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_theme

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.about.navigateAbout

/**
 * [addSettingThemeScreen]
 *
 * @author why
 * @since 5/8/24
 */
private const val ROUTE_SETTINGS_THEME = "settings-theme"

fun NavController.navigateSettingTheme() {
    navigate(ROUTE_SETTINGS_THEME)
}

fun NavGraphBuilder.addSettingThemeScreen(navController: NavController) {
    composable(route = ROUTE_SETTINGS_THEME) {
        SettingThemeRoute(
            onNavUp = { navController.popBackStack() },
            onNavAboutScreen = { navController.navigateAbout() }
        )
    }
}
