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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.LocalThemeConfigState
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.component.SettingNormalItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingOptionsItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingSwitchItem
import com.xiaoyv.flexiflix.common.config.settings.AppSettings

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
        onNavUp = onNavUp,
        onNavAboutScreen = onNavAboutScreen
    )
}

@Composable
fun SettingThemeScreen(
    onNavUp: () -> Unit = {},
    onNavAboutScreen: () -> Unit = {},
) {
    val context = LocalContext.current
    val themeState = LocalThemeConfigState.current

    ScaffoldWrap(
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
                key = AppSettings.THEME_DARK_MODE_KEY,
                title = "深色模式",
                formatClass = requireNotNull(Int::class.javaPrimitiveType),
                default = AppSettings.THEME_DARK_MODE_VALUE_SYSTEM,
                values = remember {
                    mapOf(
                        "跟随系统" to AppSettings.THEME_DARK_MODE_VALUE_SYSTEM,
                        "开启" to AppSettings.THEME_DARK_MODE_VALUE_ON,
                        "关闭" to AppSettings.THEME_DARK_MODE_VALUE_OFF,
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


            SettingSwitchItem(
                key = AppSettings.THEME_DARK_PURE_KEY,
                title = "深色模式下纯黑主题",
                default = AppSettings.THEME_DARK_PURE_VALUE_DEFAULT,
                icon = {
                    Icon(
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

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
                items(10) {
                    SettingThemePreview(
                        modifier = Modifier,
                        selected = false,
                    )
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
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 411, heightDp = 711)
@Composable
fun PreviewSettingThemeScreen() {
    SettingThemeScreen()
}