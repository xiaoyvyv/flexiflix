package com.xiaoyv.comic.flexiflix.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.xiaoyv.comic.flexiflix.ui.animation.MotionConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface PopupHostState {
    val visible: Boolean
    val content: @Composable ColumnScope.() -> Unit
    fun show(content: @Composable ColumnScope.() -> Unit)
    fun hide()
}

internal class PopupHostStateImpl(private val scope: CoroutineScope) : PopupHostState {
    private var cleanJob: Job? = null

    override var visible: Boolean by mutableStateOf(false)
        private set

    override var content: (@Composable ColumnScope.() -> Unit) by mutableStateOf(NoContent)
        private set

    override fun show(content: @Composable ColumnScope.() -> Unit) {
        cleanJob?.cancel()
        cleanJob = null

        this.content = content
        this.visible = true
    }

    override fun hide() {
        this.visible = false

        cleanJob?.cancel()
        cleanJob = this.scope.launch {
            delay(MotionConstants.DefaultMotionDuration.toLong())
            content = NoContent
        }
    }

    private companion object {
        private val NoContent: (@Composable ColumnScope.() -> Unit) = {}
    }
}

@Composable
internal fun rememberPopupHostState(): PopupHostState {
    val scope = rememberCoroutineScope()
    return remember { PopupHostStateImpl(scope) }
}

val LocalPopupHostState = staticCompositionLocalOf<PopupHostState> {
    error("LocalSnackbarHostState not present")
}
