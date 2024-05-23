package com.xiaoyv.comic.flexiflix.ui.screen.main.source

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.xiaoyv.comic.flexiflix.model.InstalledMediaSource
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.component.StringLabelPage
import com.xiaoyv.comic.flexiflix.ui.component.TabPager
import com.xiaoyv.comic.flexiflix.ui.screen.main.source.installed.SourceInstalledTabRoute
import com.xiaoyv.comic.flexiflix.ui.screen.main.source.online.SourceOnlineTabRoute
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme

/**
 * [MainSourceScreen]
 *
 * @author why
 * @since 5/8/24
 */
@Composable
fun MainSourceRoute(
    onSourceClick: (InstalledMediaSource) -> Unit,
) {
    val viewModel = hiltViewModel<MainSourceViewModel>()
    val pickExtensionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            viewModel.installExtension(it ?: return@rememberLauncherForActivityResult)
        }

//    var refreshState by remember { mutableIntStateOf(0) }


    // 插件安装状态提示
//    val hostState = LocalSnackbarHostState.current
//    val installState by viewModel.installState.collectAsStateWithLifecycle()
//    LaunchedEffect(installState) {
//        if (installState != null) {
//            refreshState++
//
//            hostState.showSnackbar(if (requireNotNull(installState).isSuccess) "安装成功" else "安装失败")
//        }
//    }
//
//    // 清除安装提示
//    LifecycleEventEffect(event = Lifecycle.Event.ON_STOP) {
//        viewModel.clearInstallState()
//    }

    MainSourceScreen(
//        refreshListState = refreshState,
        onSourceClick = onSourceClick,
        onPickExtensionClick = {
            pickExtensionLauncher.launch("application/vnd.android.package-archive")
        }
    )
}

@Composable
fun MainSourceScreen(
//    refreshListState: Any,
    onSourceClick: (InstalledMediaSource) -> Unit,
    onPickExtensionClick: () -> Unit = {},
) {
    val labelPages = remember {
        listOf(
            StringLabelPage(label = "媒体源") {
                SourceInstalledTabRoute(
                    onSourceClick = onSourceClick,
//                    refreshListState = refreshListState
                )
            },
            StringLabelPage(label = "插件") {
                SourceOnlineTabRoute()
            }
        )
    }

    ScaffoldWrap(
        topBar = {
            AppBar(
                title = "数据源插件",
                hideNavigationIcon = true,
                actions = {
                    IconButton(onClick = onPickExtensionClick) {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        TabPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            labelPages = labelPages
        )
    }
}


@Composable
@Preview(widthDp = 411, heightDp = 711)
fun PreviewMainSourceScreen() {
    AppTheme {
        MainSourceScreen(
            onSourceClick = {},
        )
    }
}