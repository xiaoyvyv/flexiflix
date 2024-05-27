package com.xiaoyv.comic.flexiflix.ui.screen.feature.search.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
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
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem

/**
 * [MediaSearchResultScreen]
 *
 * @author why
 * @since 5/20/24
 */
@Composable
fun MediaSearchResultRoute(
    onNavUp: () -> Unit = {},
    onSectionMediaClick: (String, FlexMediaSectionItem) -> Unit = { _, _ -> },
) {
    val viewModel = hiltViewModel<MediaSearchResultViewModel>()
    val pagingItems = viewModel.searchSource.collectAsLazyPagingItems()

    MediaSearchResultScreen(
        pagingItems = pagingItems,
        onNavUp = onNavUp,
        onSectionMediaClick = {
            onSectionMediaClick(viewModel.args.sourceId, it)
        }
    )
}

@Composable
fun MediaSearchResultScreen(
    pagingItems: LazyPagingItems<FlexMediaSectionItem>,
    onNavUp: () -> Unit = {},
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit = {},
) {
    ScaffoldScreen(
        topBar = {
            AppBar(title = "搜索结果", onNavigationIconClick = onNavUp)
        }
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(horizontal = 8.dp),
            columns = GridCells.Fixed(2),
        ) {
            items(pagingItems.itemCount) { index ->
                val item = pagingItems[index]
                if (item != null) {
                    MediaSearchResultItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 12.dp),
                        media = item,
                        onSectionMediaClick = onSectionMediaClick
                    )
                }
            }
        }
    }
}

/**
 * 搜索结果的单个条目
 */
@Composable
fun MediaSearchResultItem(
    modifier: Modifier = Modifier,
    media: FlexMediaSectionItem,
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth()) {
            ElevatedImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(media.requireLayout.aspectRatio),
                overlayBrush = Brush.verticalGradient(
                    remember {
                        listOf(
                            Color.Black.copy(alpha = 0f),
                            Color.Black.copy(alpha = 0.1f),
                            Color.Black.copy(alpha = 0.9f),
                        )
                    }
                ),
                model = media.cover,
                onClick = { onSectionMediaClick(media) },
            )

            if (media.requireOverlay.topStart.orEmpty().isNotBlank()) Text(
                modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 6.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(4.dp)
                    .align(Alignment.TopStart),
                text = media.requireOverlay.topStart.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary
            )

            if (media.requireOverlay.topEnd.orEmpty().isNotBlank()) Text(
                modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 6.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(4.dp)
                    .align(Alignment.TopEnd),
                text = media.requireOverlay.topEnd.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 6.dp)
                    .align(Alignment.BottomStart),
                text = media.requireOverlay.bottomStart.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 6.dp)
                    .align(Alignment.BottomEnd),
                text = media.requireOverlay.bottomEnd.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            text = media.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
@Preview(widthDp = 411, heightDp = 700)
fun PreviewMediaSearchResultScreen() {
    val data = List(10) {
        FlexMediaSectionItem(
            id = "",
            title = "测试：$it",
            cover = "",
            description = ""
        )
    }

    val fake = mutableStateFlowOf(PagingData.from(data))

    MediaSearchResultScreen(
        pagingItems = fake.collectAsLazyPagingItems()
    )
}