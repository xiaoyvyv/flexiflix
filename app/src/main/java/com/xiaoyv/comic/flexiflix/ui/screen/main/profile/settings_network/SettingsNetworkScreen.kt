package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_network

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mediation
import androidx.compose.material.icons.filled.NetworkCheck
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
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
import com.xiaoyv.comic.flexiflix.ui.component.SettingInputItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingNormalItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingSwitchItem
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.extension.config.settings.AppSettings

/**
 * [SettingNetworkScreen]
 *
 * @author why
 * @since 5/22/24
 */
@Composable
fun SettingNetworkRoute(
    onNavUp: () -> Unit = {},
    onNavAboutScreen: () -> Unit = {},
) {
    SettingNetworkScreen(
        onNavUp = onNavUp,
        onNavAboutScreen = onNavAboutScreen
    )
}

@Composable
fun SettingNetworkScreen(
    onNavUp: () -> Unit = {},
    onNavAboutScreen: () -> Unit = {},
) {
    var rememberCustomHostState by remember { mutableStateOf(false) }

    ScaffoldScreen(
        topBar = {
            AppBar(title = "网络设置", onNavigationIconClick = onNavUp)
        }
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(it)
        ) {

            SettingSwitchItem(
                key = AppSettings.Network.NETWORK_HOST_ENABLE_KEY,
                default = AppSettings.Network.hostEnable,
                title = "启用自定义 Host",
                subtitle = "固定指定域名的 IP 解析地址",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Mediation,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onValueChange = { value ->
                    rememberCustomHostState = value
                }
            )

            if (rememberCustomHostState) {
                SettingSwitchItem(
                    key = AppSettings.Network.NETWORK_HOST_EXTENSION_ENABLE_KEY,
                    default = AppSettings.Network.hostExtensionEnable,
                    title = "启用插件 Host 配置",
                    subtitle = "部分插件源自带 Host 配置",
                    icon = {
                        Icon(
                            modifier = Modifier.alpha(0f),
                            imageVector = Icons.Default.Mediation,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )

                SettingInputItem(
                    key = AppSettings.Network.NETWORK_HOST_CONTENT_KEY,
                    default = AppSettings.Network.hostContent,
                    title = "Host",
                    subtitle = "自定义 Host",
                    icon = {
                        Icon(
                            modifier = Modifier.alpha(0f),
                            imageVector = Icons.Default.Mediation,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }

            SettingNormalItem(
                title = "代理",
                subtitle = "配置网络代理",
                icon = {
                    Icon(
                        imageVector = Icons.Default.NetworkCheck,
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
fun PreviewSettingNetworkScreen() {
    AppTheme {
        SettingNetworkScreen()
    }
}