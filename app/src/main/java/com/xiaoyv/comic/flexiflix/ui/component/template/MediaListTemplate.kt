package com.xiaoyv.comic.flexiflix.ui.component.template

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.xiaoyv.comic.flexiflix.ui.component.ElevatedImage
import com.xiaoyv.comic.flexiflix.ui.component.LazyList
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem

/**
 * 针对 [FlexMediaSectionItem] 的可调整列表或宫格的 List 模板
 *
 * @author why
 * @since 5/21/24
 */
@Composable
fun MediaListTemplate(
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<FlexMediaSectionItem>,
    columns: Int = 3,
    onMediaClick: (FlexMediaSectionItem) -> Unit = {},
) {
    if (columns == 1) {
        LazyList(modifier = modifier) {
            items(pagingItems.itemCount) { index ->
                MediaListTempListItem(
                    modifier = itemModifier.fillMaxWidth(),
                    item = requireNotNull(pagingItems[index]),
                    onMediaClick = onMediaClick
                )
            }
        }
    } else {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(columns)
        ) {
            items(pagingItems.itemCount) { index ->
                MediaListTempGridItem(
                    modifier = itemModifier.fillMaxWidth(),
                    item = requireNotNull(pagingItems[index]),
                    onMediaClick = onMediaClick
                )
            }
        }
    }
}


/**
 * 列表类型的条目
 */
@Composable
fun MediaListTempListItem(
    modifier: Modifier = Modifier,
    item: FlexMediaSectionItem,
    onMediaClick: (FlexMediaSectionItem) -> Unit = {},
) {
    val hasCover = item.cover.isNotBlank()

    ConstraintLayout(
        modifier = Modifier
            .clickable { onMediaClick(item) }
            .then(modifier)
    ) {
        val (cover, title, desc, other) = createRefs()

        if (hasCover) {
            MediaOverlayImage(
                modifier = Modifier.constrainAs(cover) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                imageModifier = Modifier
                    .width(100.dp)
                    .aspectRatio(3 / 4f),
                item = item,
                onMediaClick = onMediaClick
            )
        } else {
            MediaOverlayFlowText(
                modifier = Modifier.constrainAs(other) {
                    top.linkTo(desc.bottom, 8.dp)
                    start.linkTo(title.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                item = item
            )
        }

        Text(
            modifier = Modifier
                .constrainAs(title) {
                    linkTo(
                        start = if (hasCover) cover.end else parent.start,
                        startMargin = if (hasCover) 12.dp else 0.dp,
                        bias = 0f,
                        end = parent.end,
                    )
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
            text = item.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )


        Text(
            modifier = Modifier
                .constrainAs(desc) {
                    start.linkTo(title.start)
                    end.linkTo(parent.end)
                    top.linkTo(title.bottom, 6.dp)
                    width = Dimension.fillToConstraints
                },
            text = item.description ?: "该内容暂时没有描述",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * 宫格类型的条目
 */
@Composable
fun MediaListTempGridItem(
    modifier: Modifier = Modifier,
    item: FlexMediaSectionItem,
    onMediaClick: (FlexMediaSectionItem) -> Unit = {},
) {
    val hasCover = item.cover.isNotBlank()

    Column(
        modifier = modifier
            .then(
                if (hasCover) Modifier else {
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(12.dp)
                }
            )
    ) {
        if (hasCover) {
            MediaOverlayImage(
                imageModifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(item.requireLayout.aspectRatio),
                item = item,
                onMediaClick = onMediaClick
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (hasCover) 8.dp else 0.dp),
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        if (!hasCover) {
            MediaOverlayFlowText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                item = item
            )
        }
    }
}

/**
 * 没有封面的情况 [FlexMediaSectionItem.overlay] 的展示样式
 */
@Composable
fun MediaOverlayFlowText(
    modifier: Modifier,
    item: FlexMediaSectionItem,
) {
    val overlay = remember(item.requireOverlay) {
        listOf(
            item.requireOverlay.topStart,
            item.requireOverlay.topEnd,
            item.requireOverlay.bottomStart,
            item.requireOverlay.bottomEnd,
        ).filter { !it.isNullOrBlank() }
    }

    if (overlay.isNotEmpty()) {
        FlowRow(modifier = modifier) {
            overlay.forEach {
                Text(
                    modifier = Modifier
                        .padding(end = 8.dp, bottom = 8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                        .padding(horizontal = 8.dp)
                        .height(24.dp),
                    text = it.orEmpty(),
                    lineHeight = 24.sp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/**
 * 有封面的情况 [FlexMediaSectionItem.overlay] 的展示样式
 */
@Composable
fun MediaOverlayImage(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    item: FlexMediaSectionItem,
    onMediaClick: (FlexMediaSectionItem) -> Unit = {},
) {
    ConstraintLayout(modifier = modifier) {
        val (cover, coverTopStart, coverTopEnd, coverBottomStart, coverBottomEnd) = createRefs()

        ElevatedImage(
            modifier = imageModifier.constrainAs(cover) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
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
            onClick = { onMediaClick(item) },
        )

        if (!item.requireOverlay.topStart.isNullOrBlank()) Text(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 6.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
                .padding(4.dp)
                .constrainAs(coverTopStart) {
                    top.linkTo(cover.top)
                    start.linkTo(cover.start)
                },
            text = item.requireOverlay.topStart.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )

        if (!item.requireOverlay.topEnd.isNullOrBlank()) Text(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 6.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
                .padding(4.dp)
                .constrainAs(coverTopEnd) {
                    top.linkTo(cover.top)
                    end.linkTo(cover.end)
                },
            text = item.requireOverlay.topEnd.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )


        if (!item.requireOverlay.bottomStart.isNullOrBlank()) Text(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 6.dp)
                .constrainAs(coverBottomStart) {
                    bottom.linkTo(cover.bottom)
                    start.linkTo(cover.start)
                },
            text = item.requireOverlay.bottomStart.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )

        if (!item.requireOverlay.bottomEnd.isNullOrBlank()) Text(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 6.dp)
                .constrainAs(coverBottomEnd) {
                    bottom.linkTo(cover.bottom)
                    end.linkTo(cover.end)
                },
            text = item.requireOverlay.bottomEnd.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}


@Composable
@Preview(widthDp = 411, heightDp = 711)
fun PreviewMediaListTemplate() {
    val items = List(10) {
        FlexMediaSectionItem(
            id = "1",
            title = "希尔达第一季希尔达第一季希尔达第一季",
            description = "改编自英国漫画家LukePearson的同名系列漫画，讲述勇敢的蓝发女孩希尔达的冒险经历。希尔达的家原本在精灵和巨人出没的荒野，她的宠物是一只小鹿狐，名叫枝枝。可现在希尔达要搬家了，要搬到大城市Trolberg去，在那里她将遇见新的朋友，展开新的冒险，还会碰到超乎她想象的奇怪甚至危险的神秘生物。",
//            cover = "http://css.yhdmtu.xyz/news/2023/10/07/20220095.jpg",
            cover = "",
            overlay = FlexMediaSectionItem.OverlayText(
                topStart = "左上",
                topEnd = "111",
                bottomEnd = "右下",
                bottomStart = "左下",
            )
        )
    }

    val pagingItems =
        mutableStateFlowOf(PagingData.from(items)).collectAsLazyPagingItems()

    MediaListTemplate(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        itemModifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        pagingItems = pagingItems,
        columns = 2,
    )
}