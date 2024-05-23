package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Dry
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.component.SettingNormalItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingOptionsItem
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.common.config.settings.AppSettings

/**
 * [SettingPlayerScreen]
 *
 * @author why
 * @since 5/22/24
 */
@Composable
fun SettingPlayerRoute(
    onNavUp: () -> Unit = {},
    onNavAboutScreen: () -> Unit = {},
) {
    SettingPlayerScreen(
        onNavUp = onNavUp,
        onNavAboutScreen = onNavAboutScreen
    )
}

@Composable
fun SettingPlayerScreen(
    onNavUp: () -> Unit = {},
    onNavAboutScreen: () -> Unit = {},
) {

    ScaffoldWrap(
        topBar = {
            AppBar(title = "播放器设置", onNavigationIconClick = onNavUp)
        }
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(it)
        ) {

            SettingOptionsItem(
                key = AppSettings.Player.PLAYER_PORTRAIT_RATIO_KEY,
                title = "播放器竖屏状态宽高比",
                formatClass = requireNotNull(Float::class.javaPrimitiveType),
                default = remember { AppSettings.Player.portraitRatio },
                values = remember {
                    mapOf(
                        "4:3" to AppSettings.Player.PLAYER_PORTRAIT_RATIO_VALUE_4_3,
                        "15:10" to AppSettings.Player.PLAYER_PORTRAIT_RATIO_VALUE_15_10,
                        "16:9" to AppSettings.Player.PLAYER_PORTRAIT_RATIO_VALUE_16_9,
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.FitScreen,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            SettingOptionsItem(
                key = AppSettings.Player.PLAYER_PRESS_SPEED_KEY,
                title = "长按倍数播放速率",
                subtitle = { _, value -> "%.1f 倍速".format(value) },
                formatClass = requireNotNull(Float::class.javaPrimitiveType),
                default = remember { AppSettings.Player.pressSpeed },
                values = remember {
                    mapOf(
                        "1.5 倍速" to AppSettings.Player.PLAYER_PRESS_SPEED_VALUE_1_5,
                        "2.0 倍速" to AppSettings.Player.PLAYER_PRESS_SPEED_VALUE_2,
                        "2.5 倍速" to AppSettings.Player.PLAYER_PRESS_SPEED_VALUE_2_5,
                        "3.0 倍速" to AppSettings.Player.PLAYER_PRESS_SPEED_VALUE_3,
                        "3.5 倍速" to AppSettings.Player.PLAYER_PRESS_SPEED_VALUE_3_5,
                        "4.0 倍速" to AppSettings.Player.PLAYER_PRESS_SPEED_VALUE_4,
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            SettingOptionsItem(
                key = AppSettings.Player.PLAYER_DRAG_SENSITIVITY_KEY,
                title = "手势左右拖拽进度灵敏度",
                formatClass = requireNotNull(Float::class.javaPrimitiveType),
                default = remember { AppSettings.Player.dragSensitivity },
                values = remember {
                    mapOf(
                        "低" to AppSettings.Player.PLAYER_DRAG_SENSITIVITY_VALUE_LOWEST,
                        "稍低" to AppSettings.Player.PLAYER_DRAG_SENSITIVITY_VALUE_LOWER,
                        "正常" to AppSettings.Player.PLAYER_DRAG_SENSITIVITY_VALUE_MIDDLE,
                        "稍高" to AppSettings.Player.PLAYER_DRAG_SENSITIVITY_VALUE_HIGHER,
                        "高" to AppSettings.Player.PLAYER_DRAG_SENSITIVITY_VALUE_HIGHEST,
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Dry,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            SettingNormalItem(
                title = "清理缓存",
                subtitle = "128MB",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Cached,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    }
}


@Preview(widthDp = 411, heightDp = 711)
@Composable
fun PreviewSettingPlayerScreen() {
    AppTheme {
        SettingPlayerScreen()
    }
}