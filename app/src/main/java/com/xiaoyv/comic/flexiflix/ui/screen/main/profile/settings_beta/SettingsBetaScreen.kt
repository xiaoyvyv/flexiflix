package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_beta

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ClosedCaptionDisabled
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Mediation
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.UpdateDisabled
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.component.SettingNormalItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingOptionsItem
import com.xiaoyv.comic.flexiflix.ui.component.SettingSwitchItem
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.common.config.settings.AppSettings
import com.xiaoyv.flexiflix.extension.utils.versionCode
import com.xiaoyv.flexiflix.extension.utils.versionName

/**
 * [SettingBetaScreen]
 *
 * @author why
 * @since 5/22/24
 */
@Composable
fun SettingBetaRoute(
    onNavUp: () -> Unit = {},
    onNavAboutScreen: () -> Unit = {},
) {
    SettingBetaScreen(
        onNavUp = onNavUp,
        onNavAboutScreen = onNavAboutScreen
    )
}

@Composable
fun SettingBetaScreen(
    onNavUp: () -> Unit = {},
    onNavAboutScreen: () -> Unit = {},
) {
    val context = LocalContext.current

    ScaffoldWrap(
        topBar = {
            AppBar(title = "实验室", onNavigationIconClick = onNavUp)
        }
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(it)
        ) {

            SettingSwitchItem(
                key = AppSettings.Beta.BETA_BLOCK_M3U8_AD_KEY,
                default = AppSettings.Beta.blockM3u8Ad,
                title = "过滤 M3U8 数据源广告",
                subtitle = "通过离群检测算法过滤掉广告片段",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Block,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            SettingOptionsItem(
                key = AppSettings.Beta.BETA_CROP_TOP_KEY,
                default = AppSettings.Beta.cropTop,
                title = "裁除顶部轮播广告",
                subtitle = { label, _ -> "%s 顶部高度像素".format(label) },
                formatClass = requireNotNull(Float::class.javaPrimitiveType),
                values = remember {
                    mapOf(
                        "不裁剪" to AppSettings.Beta.BETA_CROP_TOP_VALUE_0,
                        "2.5%" to AppSettings.Beta.BETA_CROP_TOP_VALUE_0_025,
                        "5%" to AppSettings.Beta.BETA_CROP_TOP_VALUE_0_05,
                        "7.5%" to AppSettings.Beta.BETA_CROP_TOP_VALUE_0_075,
                        "10%" to AppSettings.Beta.BETA_CROP_TOP_VALUE_0_1,
                        "12.5%" to AppSettings.Beta.BETA_CROP_TOP_VALUE_0_125,
                        "15%" to AppSettings.Beta.BETA_CROP_TOP_VALUE_0_15,
                        "17.5%" to AppSettings.Beta.BETA_CROP_TOP_VALUE_0_175,
                        "20%" to AppSettings.Beta.BETA_CROP_TOP_VALUE_0_2,
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
        }
    }
}


@Preview(widthDp = 411, heightDp = 711)
@Composable
fun PreviewSettingBetaScreen() {
   AppTheme {
       SettingBetaScreen()
   }
}