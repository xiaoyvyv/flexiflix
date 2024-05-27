package com.xiaoyv.comic.flexiflix.ui.screen.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.GlideImage
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.ElevatedImage
import com.xiaoyv.comic.flexiflix.ui.component.StateScreen
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldRefresh
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
import com.xiaoyv.comic.flexiflix.ui.component.StringLabelPage
import com.xiaoyv.comic.flexiflix.ui.component.PlayingAnimationBar
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.common.utils.isNotLoading
import com.xiaoyv.flexiflix.common.utils.isStoped
import com.xiaoyv.flexiflix.extension.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * [MediaHomeScreen]
 *
 * @author why
 * @since 5/8/24
 */
@Composable
fun MediaHomeRoute(
    onSectionClick: (String, FlexMediaSection) -> Unit,
    onSectionMediaClick: (String, FlexMediaSectionItem) -> Unit,
    onSearchClick: (String, String) -> Unit = { _, _ -> },
) {
    val viewModel = hiltViewModel<MediaHomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MediaHomeScreen(
        title = viewModel.args.sourceName,
        uiState = uiState,
        onRefresh = { viewModel.refresh() },
        onSectionMediaClick = {
            onSectionMediaClick(viewModel.args.sourceId, it)
        },
        onSectionClick = {
            onSectionClick(viewModel.args.sourceId, it)
        },
        onSearchClick = {
            onSearchClick(viewModel.args.sourceId, viewModel.args.sourceName)
        }
    )
}

@Composable
fun MediaHomeScreen(
    title: String,
    uiState: MediaHomeState,
    onRefresh: () -> Unit = {},
    onSectionClick: (FlexMediaSection) -> Unit = {},
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit = {},
    onSearchClick: () -> Unit = {},
) {
    val refreshState = rememberPullToRefreshState()

    LaunchedEffect(uiState.loadState) {
        if (uiState.loadState.isStoped) {
            refreshState.endRefresh()
        }
    }

    val scrollState = rememberScrollState()
    val showTopBar by remember {
        derivedStateOf { scrollState.value > 250 }
    }

    ScaffoldScreen(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(refreshState.nestedScrollConnection),
        topBar = {
            AnimatedVisibility(
                visible = showTopBar,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AppBar(
                    title = title.ifBlank { "数据源详情" },
                    hideNavigationIcon = true
                )
            }

            // 右侧固定搜索按钮，不会随滑动改变
            if (uiState.loadState.isNotLoading) AppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                actions = {
                    IconButton(
                        onClick = onSearchClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = if (showTopBar) MaterialTheme.colorScheme.onSurface else Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {

        ScaffoldRefresh(
            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
            pullToRefreshState = refreshState,
            onRefresh = onRefresh
        ) {
            StateScreen(
                loadState = uiState.loadState,
                itemCount = { uiState.items.size }
            ) {
                if (uiState.items.isNotEmpty()) {
                    Column(modifier = Modifier.verticalScroll(state = scrollState)) {
                        // Banner
                        MediaHomeTopBanner(
                            banner = uiState.items.first(),
                            onSectionMediaClick = onSectionMediaClick
                        )

                        if (uiState.items.size > 1) {
                            // Sections
                            MediaHomeSections(
                                pagingItems = uiState.items,
                                onSectionClick = onSectionClick,
                                onSectionMediaClick = onSectionMediaClick
                            )
                        }
                    }
                }
            }
        }
    }
}


/**
 * 顶部 Banner 部分
 */
@Composable
fun MediaHomeTopBanner(
    banner: FlexMediaSection,
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit,
) {
    val bannerState = rememberPagerState { banner.items.size }
    val gradientColors = remember {
        listOf(
            Color.Black.copy(alpha = 0f),
            Color.Black.copy(alpha = 0.8f),
        )
    }

    val pages = remember(banner.items) {
        banner.items.map { item ->
            StringLabelPage(label = item.title) {
                ConstraintLayout(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSectionMediaClick(item) }
                ) {
                    val (bg, title, subtitle) = createRefs()

                    GlideImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .constrainAs(bg) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                                height = Dimension.ratio("16:9")
                            }
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = gradientColors,
                                        startY = 0f,
                                        endY = size.height
                                    )
                                )
                            },
                        model = item.cover,
                        contentDescription = null,
                        transition = CrossFade,
                        contentScale = ContentScale.Crop
                    )


                    Text(
                        modifier = Modifier.constrainAs(title) {
                            bottom.linkTo(subtitle.top, 8.dp)
                            start.linkTo(parent.start, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                            width = Dimension.fillToConstraints
                        },
                        text = item.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )

                    Text(
                        modifier = Modifier.constrainAs(subtitle) {
                            bottom.linkTo(parent.bottom, 16.dp)
                            start.linkTo(parent.start, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                            width = Dimension.fillToConstraints
                        },
                        text = item.description.orEmpty(),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

    HorizontalPager(state = bannerState) {
        pages[it].content()
    }

    // 拖动时取消轮播计时器
    val isDragged by bannerState.interactionSource.collectIsDraggedAsState()
    if (!isDragged) {
        LaunchedEffect(Unit) {
            while (isActive) {
                delay(2500)
                bannerState.animateScrollToPage(page = (bannerState.currentPage + 1) % bannerState.pageCount)
            }
        }
    }
}


/**
 * Sections 部分
 */
@Composable
fun MediaHomeSections(
    pagingItems: List<FlexMediaSection>,
    onSectionClick: (FlexMediaSection) -> Unit,
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit,
) {
    for (i in 1 until pagingItems.size) {
        MediaHomeSectionItem(
            item = pagingItems[i],
            onSectionClick = onSectionClick,
            onSectionMediaClick = onSectionMediaClick
        )
    }
}

@Composable
fun MediaHomeSectionItem(
    item: FlexMediaSection,
    onSectionClick: (FlexMediaSection) -> Unit,
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)
                    .clickable {
                        onSectionClick(item)
                    },
                text = item.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.weight(1f))

            TextButton(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(bottom = 8.dp, end = 4.dp),
                onClick = { onSectionClick(item) }
            ) {
                Text(text = "更多 >>")
            }
        }

        MediaHomeSectionItemRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            itemModifier = Modifier.padding(horizontal = 8.dp),
            items = item.items,
            onSectionMediaClick = onSectionMediaClick
        )
    }
}

@Composable
fun MediaHomeSectionItemRow(
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier,
    currentMediaId: String? = null,
    items: List<FlexMediaSectionItem>,
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit,
) {
    LazyRow(modifier = modifier) {
        items(items) { media ->
            Column(modifier = itemModifier.width(media.requireLayout.widthDp.dp)) {
                val isCurrentMedia = currentMediaId != null && media.id == currentMediaId

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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 正在播放中
                    if (isCurrentMedia) {
                        PlayingAnimationBar(sizeDp = 16f)
                    }

                    Text(
                        modifier = Modifier.weight(1f),
                        text = media.title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (isCurrentMedia) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


@Composable
@Preview(widthDp = 411, heightDp = 711)
fun PreviewMediaHomeScreen() {
    val items = listOf(
        FlexMediaSectionItem(
            id = "",
            title = "媒体条目1",
            description = "你好",
            cover = "",
            overlay = FlexMediaSectionItem.OverlayText(
                topStart = "左上角",
                topEnd = "右上角",
                bottomStart = "左下角",
                bottomEnd = "右下角"
            ),
            layout = FlexMediaSectionItem.ImageLayout(
                widthDp = 120,
                aspectRatio = 3 / 4f
            )
        ),
        FlexMediaSectionItem(
            id = "",
            title = "媒体条目1",
            description = "你好",
            cover = "",
            overlay = FlexMediaSectionItem.OverlayText(
                topStart = "左上角",
                topEnd = "右上角",
                bottomStart = "左下角",
                bottomEnd = "右下角"
            ),
        ),
        FlexMediaSectionItem(
            id = "",
            title = "媒体条目1",
            description = "你好",
            cover = "",
            overlay = FlexMediaSectionItem.OverlayText(
                topStart = "左上角",
                topEnd = "右上角",
                bottomStart = "左下角",
                bottomEnd = "右下角"
            )
        ),
    )

    AppTheme {
        MediaHomeScreen(
            title = "数据源详情",
            uiState = MediaHomeState(
                loadState = LoadState.NotLoading(true),
                items = listOf(
                    FlexMediaSection(
                        id = "",
                        title = "测试",
                        items = items
                    ),
                    FlexMediaSection(
                        id = "",
                        title = "测试",
                        items = items
                    ),
                    FlexMediaSection(
                        id = "",
                        title = "测试",
                        items = items
                    ),
                ),
            )
        )
    }
}