package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.extension


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.xiaoyv.comic.flexiflix.R
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
import com.xiaoyv.comic.flexiflix.ui.component.SettingNormalItem

/**
 * [ExtensionCompatScreen]
 *
 * @author why
 * @since 5/22/24
 */
@Composable
fun ExtensionCompatRoute(
    onNavUp: () -> Unit = {},
) {
    ExtensionCompatScreen(
        onNavUp = onNavUp,
    )
}

@Composable
fun ExtensionCompatScreen(
    onNavUp: () -> Unit = {},
) {
    val context = LocalContext.current

    ScaffoldScreen(
        topBar = {
            AppBar(title = "插件兼容性", onNavigationIconClick = onNavUp)
        }
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(it)
        ) {

            SettingNormalItem(
                title = "Java | Kotlin 插件",
                subtitle = "支持 AndroidStdio 开发插件",
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_android),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
            )


            SettingNormalItem(
                title = "JavaScript 插件",
                subtitle = "支持 NodeJs 生态开发插件",
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_nodejs),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
            )

            SettingNormalItem(
                title = "Python 插件",
                subtitle = "支持 Python3.8 开发插件",
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_python),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
            )
        }
    }
}


@Preview(widthDp = 411, heightDp = 711)
@Composable
fun PreviewExtensionCompatScreen() {
    ExtensionCompatScreen()
}