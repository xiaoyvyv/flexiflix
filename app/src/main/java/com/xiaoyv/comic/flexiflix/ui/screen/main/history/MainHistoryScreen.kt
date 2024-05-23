package com.xiaoyv.comic.flexiflix.ui.screen.main.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ElevatedImage
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.common.database.collect.CollectionEntity
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf

/**
 * [MainHistoryScreen]
 *
 * @author why
 * @since 5/8/24
 */
@Composable
fun MainHistoryRoute(
    onMediaClick: (String, String) -> Unit = { _, _ -> },
) {
    val viewModel = hiltViewModel<MainHistoryViewModel>()
    val pagingItems = viewModel.pageSource.collectAsLazyPagingItems()

    MainHistoryScreen(
        pagingItems = pagingItems,
        onMediaClick = onMediaClick
    )
}

@Composable
fun MainHistoryScreen(
    pagingItems: LazyPagingItems<CollectionEntity>,
    onMediaClick: (String, String) -> Unit = { _, _ -> },
) {
    ScaffoldWrap(
        topBar = { AppBar(title = "浏览历史", hideNavigationIcon = true) }
    ) {
        debugLog { pagingItems.itemCount.toString() }

        if (pagingItems.itemCount > 0) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 8.dp),
                columns = GridCells.Fixed(3)
            ) {
                items(pagingItems.itemCount) { index ->
                    val item = pagingItems[index]
                    if (item != null) {
                        MainHistoryItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            item = item,
                            onMediaClick = onMediaClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainHistoryItem(
    modifier: Modifier = Modifier,
    item: CollectionEntity,
    onMediaClick: (String, String) -> Unit,
) {
    Box(modifier = modifier) {
        ElevatedImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3 / 4f),
            overlayBrush = Brush.verticalGradient(
                remember {
                    listOf(
                        Color.Black.copy(alpha = 0f),
                        Color.Black.copy(alpha = 0.1f),
                        Color.Black.copy(alpha = 0.9f),
                    )
                }
            ),
            model = item.cover,
            onClick = { onMediaClick(item.sourceId, item.mediaId) },
        )

        Text(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 6.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
                .padding(4.dp)
                .align(Alignment.TopEnd),
            text = item.sourceId,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 6.dp)
                .align(Alignment.BottomStart),
            text = item.title,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}

@Composable
@Preview(widthDp = 411, heightDp = 711)
fun PreviewMainHistoryScreen() {
    val data = List(10) {
        CollectionEntity(
            sourceId = "xxxx",
            mediaId = "2",
            title = "测试",
            description = "测试",
            cover = "",
            url = ""
        )
    }

    val pagingItems =
        mutableStateFlowOf(PagingData.from(data)).collectAsLazyPagingItems()

    AppTheme {
        MainHistoryScreen(pagingItems)
    }
}