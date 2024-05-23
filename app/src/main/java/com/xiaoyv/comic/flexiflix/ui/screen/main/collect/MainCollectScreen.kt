package com.xiaoyv.comic.flexiflix.ui.screen.main.collect

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme

/**
 * [MainCollectScreen]
 *
 * @author why
 * @since 5/8/24
 */
@Composable
fun MainCollectRoute() {
    MainCollectScreen()
}

@Composable
fun MainCollectScreen() {
    ScaffoldWrap(
        topBar = { AppBar(title = "我正在追", hideNavigationIcon = true) }
    ) {


    }
}

@Composable
@Preview(widthDp = 411, heightDp = 711)
fun PreviewMainCollectScreen() {
    AppTheme {
        MainCollectScreen()
    }
}