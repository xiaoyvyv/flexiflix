package com.xiaoyv.comic.flexiflix.ui.component

import androidx.compose.runtime.staticCompositionLocalOf

interface SnackBarHostState {
    suspend fun clear()

    suspend fun showSnackBar(
        message: String,
        actionLabel: String? = null,
    )

    companion object {
        object Stub : SnackBarHostState {
            override suspend fun clear() = Unit
            override suspend fun showSnackBar(message: String, actionLabel: String?) = Unit
        }
    }
}

internal class DelegateSnackBarHostState(
    private val realSnackBarHostState: androidx.compose.material3.SnackbarHostState,
) : SnackBarHostState {
    override suspend fun clear() {
        realSnackBarHostState.currentSnackbarData?.dismiss()
    }

    override suspend fun showSnackBar(message: String, actionLabel: String?) {
        realSnackBarHostState.showSnackbar(message, actionLabel)
    }
}

val LocalSnackBarHostState = staticCompositionLocalOf<SnackBarHostState> {
    error("LocalSnackBarHostState not present")
}
