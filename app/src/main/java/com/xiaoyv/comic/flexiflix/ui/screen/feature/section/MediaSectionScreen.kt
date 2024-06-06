package com.xiaoyv.comic.flexiflix.ui.screen.feature.section

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ColumnsToggleButton
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
import com.xiaoyv.comic.flexiflix.ui.component.template.MediaListTemplate
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem

/**
 * [MediaSectionScreen]
 *
 * @author why
 * @since 5/21/24
 */
@Composable
fun MediaSectionRoute(
    onNavUp: () -> Unit = {},
    onMediaClick: (String, String) -> Unit = { _, _ -> },
) {
    val viewModel = hiltViewModel<MediaSectionViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagingItems = viewModel.sectionSource.collectAsLazyPagingItems()

    MediaSectionScreen(
        uiState = uiState,
        pagingItems = pagingItems,
        onNavUp = onNavUp,
        onMediaClick = { onMediaClick(viewModel.args.sourceId, it.id) }
    )
}

@Composable
fun MediaSectionScreen(
    uiState: MediaSectionState,
    pagingItems: LazyPagingItems<FlexMediaSectionItem>,
    onNavUp: () -> Unit = {},
    onMediaClick: (FlexMediaSectionItem) -> Unit = {},
) {
    var columns by remember { mutableIntStateOf(1) }

    ScaffoldScreen(
        topBar = {
            AppBar(
                title = uiState.title,
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
@Preview(widthDp = 411, heightDp = 711)
fun PreviewMediaSectionScreen() {
    val items = List(10) {
        FlexMediaSectionItem(
            id = "1",
            title = "希尔达第一季\n希尔达第一季\n希尔达第一季",
            description = "改编自英国漫画家LukePearson的同名系列漫画，讲述勇敢的蓝发女孩希尔达的冒险经历。希尔达的家原本在精灵和巨人出没的荒野，她的宠物是一只小鹿狐，名叫枝枝。可现在希尔达要搬家了，要搬到大城市Trolberg去，在那里她将遇见新的朋友，展开新的冒险，还会碰到超乎她想象的奇怪甚至危险的神秘生物。",
            cover = "http://css.yhdmtu.xyz/news/2023/10/07/20220095.jpg",
            overlay = FlexMediaSectionItem.OverlayText(
                topStart = "左上",
                topEnd = "右上",
                bottomEnd = "右下",
                bottomStart = "左下",
            )
        )
    }

    val pagingItems =
        mutableStateFlowOf(PagingData.from(items)).collectAsLazyPagingItems()

    MediaSectionScreen(
        uiState = MediaSectionState(
            title = "分类浏览",
            loadState = LoadState.NotLoading(true)
        ),
        pagingItems = pagingItems
    )
}