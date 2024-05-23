package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Dry
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.Mediation
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.component.SettingNormalItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingOptionsItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingSwitchItem

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
                key = "player_portrait_ratio",
                title = "播放器竖屏状态宽高比",
                formatClass = requireNotNull(Float::class.javaPrimitiveType),
                default = 4 / 3f,
                values = remember {
                    mapOf(
                        "4:3" to 4 / 3f,
                        "15:10" to 15 / 10f,
                        "16:9" to 16 / 9f
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
                key = "player_press_speed",
                title = "长按倍数播放速率",
                subtitle = { _, value -> "%.1f 倍速".format(value) },
                formatClass = requireNotNull(Float::class.javaPrimitiveType),
                default = 3f,
                values = remember {
                    mapOf(
                        "1.5 倍速" to 1.5f,
                        "2.0 倍速" to 2f,
                        "2.5 倍速" to 2.5f,
                        "3.0 倍速" to 3f,
                        "3.5 倍速" to 3.5f,
                        "4.0 倍速" to 4f,
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
                key = "player_drag_sensitivity",
                title = "手势左右拖拽进度灵敏度",
                formatClass = requireNotNull(Float::class.javaPrimitiveType),
                default = 2f,
                values = remember {
                    mapOf(
                        "低" to 4f,
                        "稍低" to 3f,
                        "正常" to 2f,
                        "稍高" to 1f,
                        "高" to 0.5f,
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
    SettingPlayerScreen()
}