package com.xiaoyv.comic.flexiflix.ui.screen.main.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.xiaoyv.comic.flexiflix.ui.component.Button
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.extension.MediaSourceFactory
import com.xiaoyv.flexiflix.extension.impl.javascript.JSExtensionService
import com.xiaoyv.flexiflix.extension.utils.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * [MainHistoryScreen]
 *
 * @author why
 * @since 5/8/24
 */
@Composable
fun MainHistoryRoute() {
    MainHistoryScreen()
}

@Composable
fun MainHistoryScreen() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ScaffoldWrap {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            Button(text = "开启服务", onClick = { JSExtensionService.instacne.startServer() })
            Button(text = "关闭服务", onClick = { JSExtensionService.instacne.stop() })
            Button(text = "重启", onClick = { JSExtensionService.instacne.restart() })
            Button(text = "获取测试扩展信息", onClick = {
                scope.launch(Dispatchers.IO) {
                    val file = File(context.filesDir, "node/extension/extension-hanime/index.js")
                    val extension = MediaSourceFactory.loadJavaScriptExtension(file.absolutePath)
                    debugLog {
                        extension.joinToString(",")
                    }
                }
            })
            Button(text = "获取首页数据", onClick = {
                scope.launch(Dispatchers.IO) {
                    val file = File(context.filesDir, "node/extension/extension-hanime/index.js")
                    val extension = MediaSourceFactory.loadJavaScriptExtension(file.absolutePath)
                    val sections = extension.first().source.fetchHomeSections()
                    debugLog {
                        sections.toJson(true)
                    }
                }
            })
        }
    }
}

@Composable
@Preview(widthDp = 411, heightDp = 711)
fun PreviewMainHistoryScreen() {
    AppTheme {
        MainHistoryScreen()
    }
}