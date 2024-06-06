package com.xiaoyv.comic.flexiflix.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import com.xiaoyv.comic.flexiflix.R
import com.xiaoyv.flexiflix.common.model.StateContent
import com.xiaoyv.flexiflix.common.model.hasData
import com.xiaoyv.flexiflix.common.utils.errMsg
import com.xiaoyv.flexiflix.i18n.I18n

@Composable
fun StateScreen(
    state: LoadState,
    listCount: () -> Int,
    emptyTitle: String = stringResource(I18n.empty_title),
    emptySubtitle: String = stringResource(I18n.empty_subtitle),
    @DrawableRes emptyImageRes: Int = R.drawable.ill_bookmarks,
    errorTitle: String = stringResource(I18n.error_title),
    errorSubtitle: String = stringResource(I18n.error_something_goes_wrong),
    @DrawableRes errorImageRes: Int = R.drawable.ill_error,
    onRetryClick: (() -> Unit)? = { },
    content: (@Composable () -> Unit)? = null,
) {
    when (state) {
        is LoadState.Loading -> {
            if (listCount() == 0) Loading(modifier = Modifier.fillMaxSize())
        }

        is LoadState.NotLoading -> {
            if (listCount() == 0) {
                Empty(
                    title = emptyTitle,
                    subtitle = emptySubtitle,
                    imageRes = emptyImageRes
                )
            } else {
                content?.invoke()
            }
        }

        is LoadState.Error -> {
            if (listCount() == 0) {
                Error(
                    title = errorTitle,
                    subtitle = state.error.errMsg.ifBlank { errorSubtitle },
                    imageRes = errorImageRes,
                    onRetryClick = onRetryClick
                )
            }
        }
    }
}

@Composable
fun <T> StateScreen(
    state: LoadState,
    stateContent: StateContent<T>,
    emptyTitle: String = stringResource(I18n.empty_title),
    emptySubtitle: String = stringResource(I18n.empty_subtitle),
    @DrawableRes emptyImageRes: Int = R.drawable.ill_bookmarks,
    errorTitle: String = stringResource(I18n.error_title),
    errorSubtitle: String = stringResource(I18n.error_something_goes_wrong),
    @DrawableRes errorImageRes: Int = R.drawable.ill_error,
    onRetryClick: (() -> Unit)? = { },
    content: (@Composable () -> Unit)? = null,
) {
    when (state) {
        is LoadState.Loading -> {
            if (!stateContent.hasData) Loading(modifier = Modifier.fillMaxSize())
        }

        is LoadState.NotLoading -> {
            if (!stateContent.hasData) {
                Empty(
                    title = emptyTitle,
                    subtitle = emptySubtitle,
                    imageRes = emptyImageRes
                )
            } else {
                content?.invoke()
            }
        }

        is LoadState.Error -> {
            if (!stateContent.hasData) {
                Error(
                    title = errorTitle,
                    subtitle = state.error.errMsg.ifBlank { errorSubtitle },
                    imageRes = errorImageRes,
                    onRetryClick = onRetryClick
                )
            }
        }
    }
}

