package com.xiaoyv.comic.flexiflix.ui.screen.feature.search.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.xiaoyv.comic.flexiflix.ui.component.ColumnsToggleButton
import com.xiaoyv.comic.flexiflix.ui.component.ElevatedImage
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
import com.xiaoyv.comic.flexiflix.ui.component.template.MediaListTemplate
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
        onMediaClick = {
            onSectionMediaClick(viewModel.args.sourceId, it)
        }
    )
}

@Composable
fun MediaSearchResultScreen(
    pagingItems: LazyPagingItems<FlexMediaSectionItem>,
    onNavUp: () -> Unit = {},
    onMediaClick: (FlexMediaSectionItem) -> Unit = {},
) {
    var columns by remember { mutableIntStateOf(1) }

    ScaffoldScreen(
        topBar = {
            AppBar(
                title = "搜索结果",
                onNavigationIconClick = onNavUp,
                actions = {
                    ColumnsToggleButton(columns = columns, onChange = { columns = it })
                }
            )
        },
    ) {
        MediaListTemplate(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 8.dp),
            itemModifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            pagingItems = pagingItems,
            columns = columns,
            onMediaClick = onMediaClick
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
            description = "测试：$it",
        )
    }

    val fake = mutableStateFlowOf(PagingData.from(data))

    MediaSearchResultScreen(
        pagingItems = fake.collectAsLazyPagingItems()
    )
}