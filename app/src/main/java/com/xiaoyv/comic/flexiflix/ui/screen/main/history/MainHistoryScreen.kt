package com.xiaoyv.comic.flexiflix.ui.screen.main.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ElevatedImage
import com.xiaoyv.comic.flexiflix.ui.component.LazyList
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
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
    ScaffoldScreen(
        topBar = {
            AppBar(title = "浏览历史", hideNavigationIcon = true)
        }
    ) {
        LazyList(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 8.dp),
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

@Composable
fun MainHistoryItem(
    modifier: Modifier = Modifier,
    item: CollectionEntity,
    onMediaClick: (String, String) -> Unit,
) {
    Row(modifier = modifier) {
        Box {
            ElevatedImage(
                modifier = Modifier
                    .width(100.dp)
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
                    .align(Alignment.BottomEnd)
                    .padding(6.dp),
                text = item.publisher.orEmpty(),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
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