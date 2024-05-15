package com.xiaoyv.comic.flexiflix.ui.screen

import androidx.compose.animation.AnimatedVisibility
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
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.screen.feature.home.navigateMediaHome
import com.xiaoyv.comic.flexiflix.ui.screen.main.history.MainHistoryRoute
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.MainProfileRoute
import com.xiaoyv.comic.flexiflix.ui.screen.main.source.MainSourceRoute

internal const val ROUTE_MAIN_TAB_HOME = "main-home"
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
    navController: NavController
) {
    MainTabScreen(
        navController = navController
    )
}

@Composable
fun MainTabScreen(
    navController: NavController
) {
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: ROUTE_MAIN_TAB_HOME

    ScaffoldWrap {
        Column(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = tabNavController,
                startDestination = ROUTE_MAIN_TAB_HOME,
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
                // 首页
                composable(ROUTE_MAIN_TAB_HOME) {
                    MainHistoryRoute()
                }

                // 数据源
                composable(ROUTE_MAIN_TAB_SOURCE) {
                    MainSourceRoute(
                        onSourceClick = {
                            navController.navigateMediaHome(it.sources.last().info.id)
                        }
                    )
                }

                // 个人中心
                composable(ROUTE_MAIN_TAB_PROFILE) {
                    MainProfileRoute()
                }
            }

            AnimatedVisibility(visible = true) {
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
    }
}