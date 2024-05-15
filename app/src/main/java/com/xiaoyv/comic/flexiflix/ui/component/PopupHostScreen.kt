package com.xiaoyv.comic.flexiflix.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xiaoyv.comic.flexiflix.ui.animation.materialFadeThroughIn
import com.xiaoyv.comic.flexiflix.ui.animation.materialFadeThroughOut

@Composable
fun PopupHostScreen(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = materialFadeThroughIn(),
        exit = materialFadeThroughOut()
    ) {
        BackHandler(onBack = onDismissRequest)
        Column(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}
