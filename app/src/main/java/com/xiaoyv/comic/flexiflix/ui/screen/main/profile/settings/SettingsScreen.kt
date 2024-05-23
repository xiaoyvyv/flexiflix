package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.component.SettingNormalItem
import com.xiaoyv.flexiflix.extension.utils.versionCode
import com.xiaoyv.flexiflix.extension.utils.versionName

/**
 * [SettingsScreen]
 *
 * @author why
 * @since 5/22/24
 */
@Composable
fun SettingsRoute(
    onNavUp: () -> Unit = {},
    onNavBetaScreen: () -> Unit = {},
    onNavNetworkScreen: () -> Unit = {},
    onNavPlayerScreen: () -> Unit = {},
    onNavThemeScreen: () -> Unit = {},
    onNavAboutScreen: () -> Unit = {},
) {
    SettingsScreen(
        onNavUp = onNavUp,
        onNavAboutScreen = onNavAboutScreen,
        onNavBetaScreen = onNavBetaScreen,
        onNavPlayerScreen = onNavPlayerScreen,
        onNavThemeScreen = onNavThemeScreen,
        onNavNetworkScreen = onNavNetworkScreen
    )
}

@Composable
fun SettingsScreen(
    onNavUp: () -> Unit = {},
    onNavBetaScreen: () -> Unit = {},
    onNavNetworkScreen: () -> Unit = {},
    onNavPlayerScreen: () -> Unit = {},
    onNavThemeScreen: () -> Unit = {},
    onNavAboutScreen: () -> Unit = {},
) {
    val context = LocalContext.current

    ScaffoldWrap(
        topBar = {
            AppBar(title = "设置", onNavigationIconClick = onNavUp)
        }
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(it)
        ) {

            SettingNormalItem(
                title = "主题外观",
                subtitle = "APP 主题外观相关配置",
                icon = {
                    Icon(
                        imageVector = Icons.Default.ColorLens,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = onNavThemeScreen
            )

            SettingNormalItem(
                title = "播放器",
                subtitle = "播放器相关配置",
                icon = {
                    Icon(
                        imageVector = Icons.Default.LiveTv,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = onNavPlayerScreen
            )

            SettingNormalItem(
                title = "网络",
                subtitle = "代理、自定义 Host 相关配置",
                icon = {
                    Icon(
                        imageVector = Icons.Default.NetworkCheck,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = onNavNetworkScreen
            )

            SettingNormalItem(
                title = "实验室",
                subtitle = "实验性功能，稳定性无法保证",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Anchor,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = onNavBetaScreen
            )

            SettingNormalItem(
                title = "关于",
                subtitle = "v%s (%d)".format(context.versionName, context.versionCode),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = onNavAboutScreen
            )
        }
    }
}


@Preview(widthDp = 411, heightDp = 711)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen()
}