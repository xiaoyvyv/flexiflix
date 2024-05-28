package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.AppThemeState
import com.xiaoyv.comic.flexiflix.ui.component.LocalThemeConfigState
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
import com.xiaoyv.comic.flexiflix.ui.component.SettingNormalItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingOptionsItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingSwitchItem
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.comic.flexiflix.ui.theme.MaterialColorTheme
import com.xiaoyv.flexiflix.extension.config.settings.AppSettings

/**
 * [SettingThemeScreen]
 *
 * @author why
 * @since 5/22/24
 */
@Composable
fun SettingThemeRoute(
    onNavUp: () -> Unit = {},
    onNavAboutScreen: () -> Unit = {},
) {
    SettingThemeScreen(
        onNavUp = onNavUp
    )
}

@Composable
fun SettingThemeScreen(
    onNavUp: () -> Unit = {},
) {
    val context = LocalContext.current
    val themeState = LocalThemeConfigState.current

    // 红、绿、蓝每个分量可以是 00、55、AA、FF 的全部组合主题色
    val themeColors = remember {
        listOf(
            "#FF0000", "#FF5500", "#FFAA00", "#FFFF00",
            "#AA0000", "#AA5500", "#AAAA00", "#AAFF00",
            "#550000", "#555500", "#55AA00", "#55FF00",
            "#000000", "#005500", "#00AA00", "#00FF00",
            "#000055", "#005555", "#00AA55", "#00FF55",
            "#0000AA", "#0055AA", "#00AAAA", "#00FFAA",
            "#0000FF", "#0055FF", "#00AAFF", "#00FFFF",
            "#5500FF", "#5555FF", "#55AAFF", "#55FFFF",
            "#AA00FF", "#AA55FF", "#AAAAFF", "#AAFFFF",
            "#FF00FF", "#FF55FF", "#FFAAFF", "#FFFFFF"
        )
    }


    ScaffoldScreen(
        topBar = {
            AppBar(title = "主题外观", onNavigationIconClick = onNavUp)
        }
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(it)
        ) {
            SettingOptionsItem(
                key = AppSettings.Theme.THEME_DARK_MODE_KEY,
                title = "深色模式",
                formatClass = requireNotNull(Int::class.javaPrimitiveType),
                default = remember { AppSettings.Theme.darkMode },
                values = remember {
                    mapOf(
                        "跟随系统" to AppSettings.Theme.THEME_DARK_MODE_VALUE_SYSTEM,
                        "开启" to AppSettings.Theme.THEME_DARK_MODE_VALUE_ON,
                        "关闭" to AppSettings.Theme.THEME_DARK_MODE_VALUE_OFF,
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.ModeNight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onValueChange = { mode ->
                    themeState.changeDarkMode(mode)
                }
            )

            // 当前主题为深色才显示该配置
            if (themeState.isDarkMode(context)) {
                SettingSwitchItem(
                    key = AppSettings.Theme.THEME_DARK_PURE_KEY,
                    title = "深色模式下纯黑主题",
                    default = remember { AppSettings.Theme.darkForceBlack },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onValueChange = { value ->
                        themeState.changeDarkForceBlack(value)
                    }
                )
            }

            SettingNormalItem(
                title = "应用主题",
                icon = {
                    Icon(
                        imageVector = Icons.Default.ColorLens,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 6.dp)
            ) {
                item { Spacer(Modifier.width(24.dp)) }
                items(themeColors.size) { index ->
                    val themeColor = themeColors[index]

                    MaterialColorTheme(themeColor) {
                        SettingThemePreview(
                            modifier = Modifier,
                            themeName = themeColor,
                            selected = AppSettings.Theme.themeColor == themeColor,
                            onClick = {
                                // 保存主题色
                                AppSettings.Theme.themeColor = themeColor

                                // 改变主题色
                                themeState.changeTheme(themeColor)
                            }
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                }
                item { Spacer(Modifier.width(24.dp)) }
            }
        }
    }
}

@Composable
fun LazyItemScope.SettingThemePreview(
    modifier: Modifier = Modifier,
    themeName: String,
    selected: Boolean,
    onClick: () -> Unit = {},
) {
    val container = MaterialTheme.colorScheme.surfaceContainer
    Column(
        modifier = modifier
            .fillParentMaxHeight()
            .aspectRatio(9f / 16f)
            .border(
                width = 4.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    container
                },
                shape = RoundedCornerShape(17.dp),
            )
            .padding(4.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = onClick),
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .weight(0.7f)
                    .padding(end = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = MaterialTheme.shapes.small,
                    ),
            )

            Box(
                modifier = Modifier.weight(0.3f),
                contentAlignment = Alignment.CenterEnd,
            ) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        // Cover
        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 2.dp)
                .background(
                    color = container,
                    shape = MaterialTheme.shapes.small,
                )
                .fillMaxWidth(0.5f)
                .aspectRatio(9 / 16f),
        ) {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .size(width = 24.dp, height = 16.dp)
                    .clip(RoundedCornerShape(5.dp)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(12.dp)
                        .background(MaterialTheme.colorScheme.tertiary),
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(12.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                )
            }
        }

        // Bottom bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Surface(
                tonalElevation = 3.dp,
            ) {
                Row(
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(17.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape,
                            ),
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .height(17.dp)
                            .weight(1f)
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = MaterialTheme.shapes.small,
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = themeName,
                            fontSize = 8.sp,
                            lineHeight = 17.sp,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 411, heightDp = 711)
@Composable
fun PreviewSettingThemeScreen() {
    CompositionLocalProvider(LocalThemeConfigState provides AppThemeState()) {
        AppTheme {
            SettingThemeScreen()
        }
    }
}