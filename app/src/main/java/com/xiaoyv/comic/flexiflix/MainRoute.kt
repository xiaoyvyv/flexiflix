package com.xiaoyv.comic.flexiflix

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.xiaoyv.comic.flexiflix.ui.animation.MotionConstants
import com.xiaoyv.comic.flexiflix.ui.animation.materialSharedAxisXIn
import com.xiaoyv.comic.flexiflix.ui.animation.materialSharedAxisXOut
import com.xiaoyv.comic.flexiflix.ui.animation.rememberSlideDistance
import com.xiaoyv.comic.flexiflix.ui.component.NavEvent
import com.xiaoyv.comic.flexiflix.ui.screen.ROUTE_MAIN_TAB
import com.xiaoyv.comic.flexiflix.ui.screen.addMainTabScreen
import com.xiaoyv.comic.flexiflix.ui.screen.feature.detail.addMediaDetailScreen
import com.xiaoyv.comic.flexiflix.ui.screen.feature.home.addMediaHomeScreen
import com.xiaoyv.comic.flexiflix.ui.screen.feature.search.addMediaSearchScreen
import com.xiaoyv.comic.flexiflix.ui.screen.feature.search.result.addMediaSearchResultScreen
import com.xiaoyv.comic.flexiflix.ui.screen.feature.section.addMediaSectionScreen
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.about.addAboutScreen
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.extension.addExtensionCompatScreen
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings.addSettingsScreen
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_beta.addSettingBetaScreen
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_network.addSettingNetworkScreen
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_player.addSettingPlayerScreen
import com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_theme.addSettingThemeScreen
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme

/**
 * [MainRoute]
 *
 * @author why
 * @since 5/8/24
 */
@Composable
fun MainRoute() {
    MainScreen()
}

@SuppressLint("RestrictedApi")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // 记录导航的事件行为
    var backStackCount by remember { mutableIntStateOf(0) }
    var lastAction by remember { mutableStateOf(NavEvent.PUSH) }
    val currentBackStack by navController.currentBackStack.collectAsState()

    // 根据回退堆栈数目判断
    LaunchedEffect(navController) {
        snapshotFlow { currentBackStack.size }.collect {
            lastAction = when {
                it > backStackCount -> NavEvent.PUSH
                it < backStackCount -> NavEvent.POP
                else -> NavEvent.REPLACE
            }
            backStackCount = it
        }
    }

    val slideDistance = rememberSlideDistance()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = ROUTE_MAIN_TAB,
        enterTransition = {
            materialSharedAxisXIn(
                forward = lastAction != NavEvent.POP,
                slideDistance = slideDistance,
                durationMillis = MotionConstants.DefaultMotionDuration,
            )
        },
        exitTransition = {
            materialSharedAxisXOut(
                forward = lastAction != NavEvent.POP,
                slideDistance = slideDistance,
                durationMillis = MotionConstants.DefaultMotionDuration,
            )
        }
    ) {

        // TAB 主页
        addMainTabScreen(navController)

        // 媒体主页
        addMediaHomeScreen(navController)

        // 媒体详情页
        addMediaDetailScreen(navController)

        // 媒体搜索页面
        addMediaSearchScreen(navController)

        // 媒体搜索结果页面
        addMediaSearchResultScreen(navController)

        // 媒体条目分类页面
        addMediaSectionScreen(navController)

        // 关于页面
        addAboutScreen(navController)

        // 插件兼容性
        addExtensionCompatScreen(navController)

        // 设置页面
        addSettingsScreen(navController)
        addSettingBetaScreen(navController)
        addSettingNetworkScreen(navController)
        addSettingPlayerScreen(navController)
        addSettingThemeScreen(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    AppTheme {
        MainScreen()
    }
}