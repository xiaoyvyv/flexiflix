package com.xiaoyv.comic.flexiflix.ui.screen.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.DisabledVisible
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
import com.xiaoyv.comic.flexiflix.ui.component.SettingNormalItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingSwitchItem
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.common.utils.openUrl
import com.xiaoyv.flexiflix.i18n.I18n

/**
 * [MainProfileScreen]
 *
 * @author why
 * @since 5/8/24
 */
@Composable
fun MainProfileRoute(
    onNavAboutScreen: () -> Unit = {},
    onNavSettingsScreen: () -> Unit = {},
    onNavTrackerScreen: () -> Unit = {},
    onNavDownloadScreen: () -> Unit = {},
    onNavExtensionCompat: () -> Unit = {},
) {
    MainProfileScreen(
        onNavAboutScreen = onNavAboutScreen,
        onNavSettingsScreen = onNavSettingsScreen,
        onNavTrackerScreen = onNavTrackerScreen,
        onNavDownloadScreen = onNavDownloadScreen,
        onNavExtensionCompat = onNavExtensionCompat,
    )
}

@Composable
fun MainProfileScreen(
    onNavAboutScreen: () -> Unit = {},
    onNavSettingsScreen: () -> Unit = {},
    onNavTrackerScreen: () -> Unit = {},
    onNavDownloadScreen: () -> Unit = {},
    onNavExtensionCompat: () -> Unit = {},
) {
    val context = LocalContext.current

    ScaffoldScreen {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(it)
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 100.dp, bottom = 60.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(I18n.app_name),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.SemiBold
            )

            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))

            SettingSwitchItem(
                key = "hide_history",
                title = "无痕模式",
                icon = {
                    Icon(
                        imageVector = Icons.Default.DisabledVisible,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            SettingNormalItem(
                title = "下载管理",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = onNavDownloadScreen
            )

            SettingNormalItem(
                title = "跟踪记录",
                icon = {
                    Icon(
                        imageVector = Icons.Default.TrackChanges,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = onNavTrackerScreen
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingNormalItem(
                title = "插件兼容性",
                subtitle = "Api 1+",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Extension,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = onNavExtensionCompat
            )

            SettingNormalItem(
                title = "设置",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = onNavSettingsScreen
            )

            SettingNormalItem(
                title = "关于",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = onNavAboutScreen
            )

            SettingNormalItem(
                title = "帮助",
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Help,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = {
                    context.openUrl("https://xiaoyvyv.github.io/flexiflix")
                }
            )
        }
    }
}


@Composable
@Preview(widthDp = 411, heightDp = 711)
fun PreviewMainProfileScreen() {
    AppTheme {
        MainProfileScreen()
    }
}