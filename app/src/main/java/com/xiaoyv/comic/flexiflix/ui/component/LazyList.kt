package com.xiaoyv.comic.flexiflix.ui.component


import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.xiaoyv.comic.flexiflix.R
import com.xiaoyv.flexiflix.i18n.I18n
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


@Composable
fun LazyList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    onFirstItemVisible: suspend () -> Unit = {},
    onLastItemVisible: suspend () -> Unit = {},
    onLastVisibleIndexChanged: suspend (Int) -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    val isFirstItemVisible by remember { derivedStateOf { state.isFirstItemVisible() } }
    LaunchedEffect(isFirstItemVisible) {
        if (isFirstItemVisible) {
            onFirstItemVisible()
        }
    }
    val isLastItemVisible by remember { derivedStateOf { state.isLastItemVisible() } }
    LaunchedEffect(isLastItemVisible) {
        if (isLastItemVisible) {
            onLastItemVisible()
        }
    }
    val lastVisibleItemIndex by remember { derivedStateOf { state.lastVisibleItemIndex() } }
    LaunchedEffect(lastVisibleItemIndex) {
        onLastVisibleIndexChanged(lastVisibleItemIndex)
    }
    val firstItem by remember {
        derivedStateOf { state.firstVisibleItemIndex }
    }
    val canScrollUp by remember(firstItem) {
        derivedStateOf { firstItem > 0 || state.firstVisibleItemScrollOffset > 0 }
    }
    val scrollState = LocalScrollState.current
    LaunchedEffect(canScrollUp) {
        scrollState.canScrollUp = canScrollUp
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            scrollState.scrollUpEvents.collect {
                Log.d("ScrollState", "Event collected")
                state.scrollToItem(0)
            }
        }
    }
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
        content = content,
    )
}

private fun LazyListState.isFirstItemVisible(): Boolean {
    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    val firstVisibleIndex = visibleItemsInfo.firstOrNull()?.index
    return visibleItemsInfo.size > 1 && firstVisibleIndex == 0
}

private fun LazyListState.isLastItemVisible(): Boolean {
    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    val lastVisibleIndex = visibleItemsInfo.lastOrNull()?.index
    val lastIndex = layoutInfo.totalItemsCount - 1
    return visibleItemsInfo.size > 1 && lastVisibleIndex == lastIndex
}

private fun LazyListState.lastVisibleItemIndex(): Int {
    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    return visibleItemsInfo.lastOrNull()?.index ?: 0
}

@Stable
internal class ScrollState {
    private val mutableScrollUpEvents = MutableSharedFlow<Unit>(replay = 1)

    var canScrollUp by mutableStateOf(false)

    val scrollUpEvents: Flow<Unit> = mutableScrollUpEvents.asSharedFlow()

    fun scrollUp(): Boolean {
        Log.d("ScrollState", "scrollUp called")
        return mutableScrollUpEvents.tryEmit(Unit)
    }
}

internal val LocalScrollState = staticCompositionLocalOf { ScrollState() }

@Composable
internal fun rememberScrollState() = remember { ScrollState() }

/*
fun LazyListScope.appendItems(
    state: LoadState,
    onRetryClick: () -> Unit,
) = when (state) {
    is LoadState.Loading -> item {
        Box(
            modifier = Modifier
                .height(AppTheme.sizes.default)
                .fillMaxWidth(),
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(AppTheme.spaces.large)
                    .size(AppTheme.sizes.medium)
                    .align(Alignment.Center),
            )
        }
    }

    is LoadState.Error -> item {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.spaces.large,
                        vertical = AppTheme.spaces.mediumLarge,
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(state.error.getStringRes()),
                )
                TextButton(
                    text = stringResource(dsR.string.designsystem_action_retry),
                    onClick = onRetryClick,
                )
            }
        }
    }

    is LoadState.NotLoading -> Unit
}*/


fun LazyListScope.loadingItem(
    fillParentMaxSize: Boolean = true,
) = item {
    Loading(
        modifier = if (fillParentMaxSize) Modifier.fillParentMaxSize() else Modifier,
    )
}


fun LazyListScope.errorItem(
    @StringRes titleRes: Int = I18n.error_title,
    @StringRes subtitleRes: Int = I18n.error_something_goes_wrong,
    @DrawableRes imageRes: Int = R.drawable.ill_error,
    fillParentMaxSize: Boolean = true,
    onRetryClick: (() -> Unit)? = null,
) = item {
    Error(
        modifier = if (fillParentMaxSize) Modifier.fillParentMaxSize() else Modifier,
        titleRes = titleRes,
        subtitleRes = subtitleRes,
        imageRes = imageRes,
        onRetryClick = onRetryClick,
    )
}

fun LazyListScope.emptyItem(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    @DrawableRes imageRes: Int,
    fillParentMaxSize: Boolean = true,
) = item {
    Empty(
        modifier = if (fillParentMaxSize) Modifier.fillParentMaxSize() else Modifier,
        titleRes = titleRes,
        subtitleRes = subtitleRes,
        imageRes = imageRes,
    )
}
