package com.xiaoyv.comic.flexiflix.ui.screen.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.GlideImage
import com.xiaoyv.comic.flexiflix.ui.component.ElevatedImage
import com.xiaoyv.comic.flexiflix.ui.component.PageStateScreen
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.component.StringLabelPage
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.common.utils.isStoped
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaSectionItem

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
) {
    val viewModel = hiltViewModel<MediaHomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MediaHomeScreen(
        uiState = uiState,
        onRefresh = { viewModel.refresh() },
        onSectionMediaClick = {
            onSectionMediaClick(viewModel.args.sourceId, it)
        },
        onSectionClick = {
            onSectionClick(viewModel.args.sourceId, it)
        }
    )
}

@Composable
fun MediaHomeScreen(
    uiState: MediaHomeState,
    onRefresh: () -> Unit,
    onSectionClick: (FlexMediaSection) -> Unit,
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit
) {
    val refreshState = rememberPullToRefreshState()
    if (refreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            onRefresh()
        }
    }

    LaunchedEffect(uiState.loadState) {
        if (uiState.loadState.isStoped) {
            refreshState.endRefresh()
        }
    }

    val scrollState = rememberScrollState()
    ScaffoldWrap(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(refreshState.nestedScrollConnection)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            PageStateScreen(
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

            // 下拉刷新
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState,
            )
        }
    }
}


/**
 * 顶部 Banner 部分
 */
@Composable
fun MediaHomeTopBanner(
    banner: FlexMediaSection,
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit
) {
    val bannerState = rememberPagerState { banner.items.size }
    val surfaceColor = MaterialTheme.colorScheme.inverseSurface
    val gradientColors = remember {
        listOf(
            surfaceColor.copy(alpha = 0f),
            surfaceColor.copy(alpha = 0.8f),
        )
    }

    val pages = remember(banner.items) {
        banner.items.map { item ->
            StringLabelPage(label = item.title) {
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
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
                        text = item.description,
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
}


/**
 * Sections 部分
 */
@Composable
fun MediaHomeSections(
    pagingItems: List<FlexMediaSection>,
    onSectionClick: (FlexMediaSection) -> Unit,
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit
) {

    for (i in 1 until pagingItems.size) {
        val item = pagingItems[i]
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
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

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                items(item.items) { media ->
                    Column(
                        modifier = Modifier
                            .width(media.layout.widthDp.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        Box(Modifier.fillMaxWidth()) {
                            ElevatedImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(media.layout.aspectRatio),
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

                            if (media.overlay.topStart.isNotBlank()) Text(
                                modifier = Modifier
                                    .padding(vertical = 6.dp, horizontal = 6.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .padding(4.dp)
                                    .align(Alignment.TopStart),
                                text = media.overlay.topStart,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onPrimary
                            )

                            if (media.overlay.topEnd.isNotBlank()) Text(
                                modifier = Modifier
                                    .padding(vertical = 6.dp, horizontal = 6.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .padding(4.dp)
                                    .align(Alignment.TopEnd),
                                text = media.overlay.topEnd,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onPrimary
                            )

                            Text(
                                modifier = Modifier
                                    .padding(vertical = 6.dp, horizontal = 6.dp)
                                    .align(Alignment.BottomStart),
                                text = media.overlay.bottomStart,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )

                            Text(
                                modifier = Modifier
                                    .padding(vertical = 6.dp, horizontal = 6.dp)
                                    .align(Alignment.BottomEnd),
                                text = media.overlay.bottomEnd,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                        }

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            text = media.title,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview(widthDp = 411, heightDp = 711)
fun PreviewMediaHomeScreen() {
    AppTheme {

        MediaHomeSections(
            pagingItems = listOf(
                FlexMediaSection(
                    id = "",
                    title = "测试",
                    items = listOf()
                ),
                FlexMediaSection(
                    id = "",
                    title = "测试",
                    items = listOf(
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
                                widthDp = 180,
                                aspectRatio = 3 / 4f
                            )
                        ),
                        FlexMediaSectionItem(
                            id = "",
                            title = "媒体条目1",
                            description = "你好",
                            cover = "",
                        ),
                    )
                ),
            ),
            onSectionMediaClick = {},
            onSectionClick = {}
        )
    }
}