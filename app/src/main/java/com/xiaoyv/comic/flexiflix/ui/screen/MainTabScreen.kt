package com.xiaoyv.comic.flexiflix.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.xiaoyv.comic.flexiflix.ui.animation.MotionConstants
import com.xiaoyv.comic.flexiflix.ui.animation.materialFadeThroughIn
import com.xiaoyv.comic.flexiflix.ui.animation.materialFadeThroughOut
import com.xiaoyv.comic.flexiflix.ui.component.BottomNavBar
import com.xiaoyv.comic.flexiflix.ui.screen.feature.detail.navigateMediaDetail
import com.xiaoyv.comic.flexiflix.ui.screen.feature.home.navigateMediaHome
import com.xiaoyv.comic.flexiflix.ui.screen.main.collect.MainCollectRoute
import com.xiaoyv.comic.flexiflix.ui.screen.main.history.MainHistoryRoute
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.MainProfileRoute
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.about.navigateAbout
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.extension.navigateExtensionCompat
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings.navigateSettings
import com.xiaoyv.comic.flexiflix.ui.screen.main.source.MainSourceRoute

internal const val ROUTE_MAIN_TAB_COLLECT = "main-collect"
internal const val ROUTE_MAIN_TAB_HISTORY = "main-history"
internal const val ROUTE_MAIN_TAB_SOURCE = "main-source"
internal const val ROUTE_MAIN_TAB_PROFILE = "main-profile"


/**
 * [MainTabScreen]
 *
 * @author why
 * @since 5/8/24
 */
@Composable
fun MainTabRoute(
    navController: NavController,
) {
    MainTabScreen(
        navController = navController
    )
}

@Composable
fun MainTabScreen(
    navController: NavController,
) {
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: ROUTE_MAIN_TAB_COLLECT

    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(
            modifier = Modifier.weight(1f),
            navController = tabNavController,
            startDestination = ROUTE_MAIN_TAB_COLLECT,
            enterTransition = {
                materialFadeThroughIn(
                    initialScale = 1f,
                    durationMillis = MotionConstants.DefaultFadeInDuration
                )
            },
            exitTransition = {
                materialFadeThroughOut(durationMillis = MotionConstants.DefaultFadeOutDuration)
            }
        ) {
            // 在追
            composable(ROUTE_MAIN_TAB_COLLECT) {
                MainCollectRoute(
                )
            }

            // 历史
            composable(ROUTE_MAIN_TAB_HISTORY) {
                MainHistoryRoute(
                    onMediaClick = { sourceId, mediaId ->
                        navController.navigateMediaDetail(sourceId, mediaId)
                    }
                )
            }

            // 数据源
            composable(ROUTE_MAIN_TAB_SOURCE) {
                MainSourceRoute(
                    onSourceClick = {
                        navController.navigateMediaHome(
                            it.sources.first().info.id,
                            it.sources.first().info.name
                        )
                    }
                )
            }

            // 个人中心
            composable(ROUTE_MAIN_TAB_PROFILE) {
                MainProfileRoute(
                    onNavAboutScreen = {
                        navController.navigateAbout()
                    },
                    onNavSettingsScreen = {
                        navController.navigateSettings()
                    },
                    onNavExtensionCompat = {
                        navController.navigateExtensionCompat()
                    }
                )
            }
        }

        BottomNavBar(
            selectedDestination = selectedDestination,
            navigateToTopLevelDestination = {
                tabNavController.navigate(it.route) {
                    popUpTo(tabNavController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}