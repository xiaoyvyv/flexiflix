package com.xiaoyv.comic.flexiflix.ui.screen.main.source.online

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

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
fun SourceOnlineTabScreen() {

}