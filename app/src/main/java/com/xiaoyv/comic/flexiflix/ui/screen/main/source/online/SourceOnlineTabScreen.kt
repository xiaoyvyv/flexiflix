package com.xiaoyv.comic.flexiflix.ui.screen.main.source.online

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.xiaoyv.comic.flexiflix.ui.component.LazyList
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldRefresh
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import kotlinx.coroutines.delay

/**
 * [SourceOnlineTabScreen]
 *
 * @author why
 * @since 5/10/24
 */

@Composable
fun SourceOnlineTabRoute() {
    val viewModel = hiltViewModel<SourceOnlineTabViewModel>()

    SourceOnlineTabScreen()
}

@Composable
fun SourceOnlineTabScreen(
    onRefresh: () -> Unit = {},
) {
    val refreshState = rememberPullToRefreshState()

    ScaffoldRefresh(
        pullToRefreshState = refreshState,
        onRefresh = {
            delay(3000)
            refreshState.endRefresh()
        },
    ) {
        LazyList(Modifier.fillMaxSize()) {
            item {
                Text(text = "xxxxxxxxxxxxxxxxxxxx\nxxxxxxxxxxxxxxx")
            }
            item {
                Text(text = "xxxxxxxxxxxxxxxxxxxx\nxxxxxxxxxxxxxxx")
            }
            item {
                Text(text = "xxxxxxxxxxxxxxxxxxxx\nxxxxxxxxxxxxxxx")
            }
        }
    }
}

@Preview(widthDp = 411, heightDp = 711)
@Composable
fun PreviewSourceOnlineTabScreen() {
    AppTheme {
        SourceOnlineTabScreen()
    }
}