package com.xiaoyv.comic.flexiflix.ui.screen.main.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme

/**
 * [MainProfileScreen]
 *
 * @author why
 * @since 5/8/24
 */
@Composable
fun MainProfileRoute() {
    MainProfileScreen()
}

@Composable
fun MainProfileScreen() {

}

@Composable
@Preview(widthDp = 411, heightDp = 711)
fun PreviewMainProfileScreen() {
    AppTheme {
        MainProfileScreen()
    }
}