package com.xiaoyv.comic.flexiflix.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.xiaoyv.comic.flexiflix.R
import com.xiaoyv.flexiflix.i18n.I18n

/**
 * [PageStateScreen]
 *
 * [PullToRefreshState] 和 [LazyPagingItems] 绑定状态视图
 *
 * @author why
 * @since 4/28/24
 */

@Composable
fun <T : Any> PageStateScreen(
    pagingItems: LazyPagingItems<T>,
    refreshState: PullToRefreshState,
    @StringRes emptyTitleRes: Int = I18n.empty_title,
    @StringRes emptySubtitleRes: Int = I18n.empty_subtitle,
    @DrawableRes emptyImageRes: Int = R.drawable.ill_bookmarks,
    @StringRes errorTitleRes: Int = I18n.error_title,
    @StringRes errorSubtitleRes: Int = I18n.error_something_goes_wrong,
    @DrawableRes errorImageRes: Int = R.drawable.ill_error,
    onRetryClick: (() -> Unit)? = { pagingItems.retry() },
) {
    pagingItems.loadState.refresh.apply {
        // 初次进入加载中...
        if (this is LoadState.Loading && !refreshState.isRefreshing) {
            Loading(modifier = Modifier.fillMaxSize())
        }
        // 刷新无数据
        if (this is LoadState.NotLoading && pagingItems.itemCount == 0) {
            Empty(
                titleRes = emptyTitleRes,
                subtitleRes = emptySubtitleRes,
                imageRes = emptyImageRes
            )
        }
        // 刷新错误
        if (this is LoadState.Error) {
            Error(
                titleRes = errorTitleRes,
                subtitleRes = errorSubtitleRes,
                imageRes = errorImageRes,
                onRetryClick = onRetryClick
            )
        }
    }
}


@Composable
fun PageStateScreen(
    loadState: LoadState,
    itemCount: () -> Int,
    @StringRes emptyTitleRes: Int = I18n.empty_title,
    @StringRes emptySubtitleRes: Int = I18n.empty_subtitle,
    @DrawableRes emptyImageRes: Int = R.drawable.ill_bookmarks,
    @StringRes errorTitleRes: Int = I18n.error_title,
    @StringRes errorSubtitleRes: Int = I18n.error_something_goes_wrong,
    @DrawableRes errorImageRes: Int = R.drawable.ill_error,
    onRetryClick: (() -> Unit)? = { },
    content: @Composable () -> Unit
) {
    when (loadState) {
        is LoadState.Loading -> {
            if (itemCount() == 0) Loading(modifier = Modifier.fillMaxSize())
        }

        is LoadState.NotLoading -> {
            if (itemCount() == 0) {
                Empty(
                    titleRes = emptyTitleRes,
                    subtitleRes = emptySubtitleRes,
                    imageRes = emptyImageRes
                )
            } else {
                content()
            }
        }

        is LoadState.Error -> {
            if (itemCount() == 0) Error(
                titleRes = errorTitleRes,
                subtitleRes = errorSubtitleRes,
                imageRes = errorImageRes,
                onRetryClick = onRetryClick
            )
        }
    }
}
