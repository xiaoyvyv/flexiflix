package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xiaoyv.comic.flexiflix.R
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
import com.xiaoyv.comic.flexiflix.ui.component.SettingNormalItem
import com.xiaoyv.flexiflix.common.utils.openUrl
import com.xiaoyv.flexiflix.extension.utils.versionCode
import com.xiaoyv.flexiflix.extension.utils.versionName
import com.xiaoyv.flexiflix.i18n.I18n

/**
 * [AboutScreen]
 *
 * @author why
 * @since 5/22/24
 */
@Composable
fun AboutRoute(
    onNavUp: () -> Unit = {},
) {
    AboutScreen(
        onNavUp = onNavUp
    )
}

@Composable
fun AboutScreen(
    onNavUp: () -> Unit = {},
) {
    val context = LocalContext.current

    ScaffoldScreen(
        topBar = {
            AppBar(title = "关于", onNavigationIconClick = onNavUp)
        }
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(it)
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 50.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(I18n.app_name),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.SemiBold
            )

            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))

            SettingNormalItem(
                title = "版本",
                subtitle = "v%s (%d)".format(context.versionName, context.versionCode),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            SettingNormalItem(
                title = "检查更新",
                icon = {
                    Icon(
                        imageVector = Icons.Default.TipsAndUpdates,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            SettingNormalItem(
                title = "更新日志",
                icon = {
                    Icon(
                        imageVector = Icons.Default.AlternateEmail,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = {
                    context.openUrl("https://github.com/xiaoyvyv/flexiflix")
                }
            )

            SettingNormalItem(
                title = "协助翻译",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = {
                    context.openUrl("https://github.com/xiaoyvyv/flexiflix")
                }
            )

            SettingNormalItem(
                title = "开源许可证",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = {
                    context.openUrl("https://github.com/xiaoyvyv/flexiflix")
                }
            )

            SettingNormalItem(
                title = "Github 项目地址",
                subtitle = "欢迎 Watch 和 Star",
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_github),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = {
                    context.openUrl("https://github.com/xiaoyvyv/flexiflix")
                }
            )

            SettingNormalItem(
                title = "隐私政策",
                icon = {
                    Icon(
                        imageVector = Icons.Default.PrivacyTip,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onItemClick = {
                    context.openUrl("https://github.com/xiaoyvyv/flexiflix")
                }
            )
        }
    }
}


@Preview(widthDp = 411, heightDp = 711)
@Composable
fun PreviewAboutScreen() {
    AboutScreen()
}