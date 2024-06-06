package com.xiaoyv.comic.flexiflix.ui.screen.feature.web

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
import com.xiaoyv.comic.flexiflix.ui.component.web.WebViewer
import com.xiaoyv.comic.flexiflix.ui.component.web.syncWebCookieToHttpClient
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import kotlinx.coroutines.launch

/**
 * [WebScreen]
 *
 * @author why
 * @since 5/29/24
 */
@Composable
fun WebRoute(onNavUp: () -> Unit = {}) {
    val viewModel = hiltViewModel<WebViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    WebScreen(
        uiState = uiState,
        onNavUp = onNavUp,
        onSyncClick = {
            scope.launch {
                syncWebCookieToHttpClient(viewModel.args.url)
                onNavUp()
            }
        }
    )
}

@Composable
fun WebScreen(
    uiState: WebState,
    onNavUp: () -> Unit = {},
    onSyncClick: () -> Unit = {},
) {
    ScaffoldScreen(
        topBar = {
            AppBar(
                title = uiState.title,
                onNavigationIconClick = onNavUp,
                actions = {
                    IconButton(onClick = onSyncClick) {
                        Icon(imageVector = Icons.Default.Done, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            var rememberProgress by remember { mutableFloatStateOf(0f) }
            val animatedProgress by animateFloatAsState(
                targetValue = rememberProgress,
                label = "web progress"
            )

            WebViewer(
                modifier = Modifier.fillMaxSize(),
                url = uiState.url,
                onProgressChange = { _, progress ->
                    rememberProgress = progress
                }
            )

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .alpha(if (animatedProgress != 1f) 1f else 0f),
            )
        }
    }
}

@Composable
@Preview(widthDp = 411, heightDp = 711)
fun PreviewWebScreen() {
    AppTheme {
        WebScreen(
            uiState = WebState()
        )
    }
}