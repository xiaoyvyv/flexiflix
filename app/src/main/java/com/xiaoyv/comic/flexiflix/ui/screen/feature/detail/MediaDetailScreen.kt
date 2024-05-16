package com.xiaoyv.comic.flexiflix.ui.screen.feature.detail

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.MediaVideoPlayer
import com.xiaoyv.comic.flexiflix.ui.component.PageStateScreen
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.component.StringLabelPage
import com.xiaoyv.comic.flexiflix.ui.component.TabPager
import com.xiaoyv.comic.flexiflix.ui.screen.feature.detail.tab.MediaDetailSummaryTab
import com.xiaoyv.flexiflix.common.model.hasData
import com.xiaoyv.flexiflix.common.model.payload
import com.xiaoyv.flexiflix.common.utils.isLandscape
import com.xiaoyv.flexiflix.common.utils.isStoped
import com.xiaoyv.flexiflix.common.utils.screenInfo
import com.xiaoyv.flexiflix.common.utils.setScreenOrientation
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylist
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylistUrl

/**
 * [MediaDetailScreen]
 *
 * @author why
 * @since 5/9/24
 */

@Composable
fun MediaDetailRoute(
    onNavUp: () -> Unit,
    onSectionMediaClick: (String, FlexMediaSectionItem) -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    val viewModel = hiltViewModel<MediaDetailViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPlayUrl by viewModel.currentPlayUrl.collectAsStateWithLifecycle()
    val currentPlaylist by viewModel.currentPlaylist.collectAsStateWithLifecycle()

    BackHandler(enabled = context.isLandscape) {
        context.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    MediaDetailScreen(
        uiState = uiState,
        currentPlayUrl = currentPlayUrl,
        currentPlaylist = currentPlaylist,
        onChangePlayItem = { list, index -> viewModel.changePlayItem(list, index) },
        onSelectPlayList = { viewModel.selectPlayList(it) },
        onRefresh = { viewModel.refresh() },
        onRetryClick = { viewModel.retry() },
        onSectionMediaClick = {
            onSectionMediaClick(viewModel.args.sourceId, it)
        },
        onNavUp = {
            if (context.isLandscape) {
                view
                    .findViewById<View>(androidx.media3.ui.R.id.exo_minimal_fullscreen)
                    .performClick()
            } else {
                onNavUp()
            }
        }
    )
}


@Composable
fun MediaDetailScreen(
    uiState: MediaDetailState,
    currentPlayUrl: FlexMediaPlaylistUrl?,
    currentPlaylist: FlexMediaPlaylist?,
    onNavUp: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    onChangePlayItem: (FlexMediaPlaylist, Int) -> Unit = { _, _ -> },
    onSelectPlayList: (FlexMediaPlaylist) -> Unit = {},
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit = {},
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // 记录播放器高度和全屏状态记录，默认宽高比 16/9f
    val playerDefaultHeight = remember { Dp((context.screenInfo.first / (16 / 9f)) / density.density) }
    var playerCurrentHeight by remember { mutableStateOf(playerDefaultHeight) }
    var playerFullScreenState by remember { mutableStateOf(false) }
    LaunchedEffect(configuration.orientation) {
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        playerFullScreenState = isLandscape
        playerCurrentHeight = if (isLandscape) {
            Dp(context.resources.displayMetrics.heightPixels.toFloat())
        } else playerDefaultHeight
    }

    // 播放器面板显示和隐藏
    var controllerVisibleState by remember { mutableStateOf(false) }

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



    ScaffoldWrap(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(refreshState.nestedScrollConnection),
        topBar = {
            AnimatedVisibility(
                visible = controllerVisibleState,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ) {

                // 切换播放条目时，更改标题
                var title by remember { mutableStateOf("媒体详情") }
                LaunchedEffect(key1 = currentPlayUrl) {
                    if (currentPlayUrl != null) {
                        title = buildString {
                            append(currentPlayUrl.title.ifBlank { "媒体详情" })
                            append(String.format("（%s)", currentPlaylist?.title))
                        }
                    }
                }

                AppBar(
                    title = title,
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    onNavigationIconClick = onNavUp
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (playerFullScreenState) 0.dp else it.calculateBottomPadding())
        ) {
            PageStateScreen(
                loadState = uiState.loadState,
                itemCount = { if (uiState.data.hasData) 1 else 0 },
                onRetryClick = onRetryClick
            ) {
                val mediaDetail = uiState.data.payload()

                // 默认播放选中第一个播放列表的第一个数据
                LaunchedEffect(Unit) {
                    if (mediaDetail.playlist.isNotEmpty()) {
                        if (mediaDetail.playlist.first().items.isNotEmpty()) {
                            onChangePlayItem(mediaDetail.playlist.first(), 0)
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    MediaVideoPlayer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(playerCurrentHeight)
                            .background(Color.Black),
                        poster = mediaDetail.cover,
                        playlistUrl = currentPlayUrl,
                        onFullscreenButtonClick = { _, fullScreen ->
                            if (fullScreen) {
                                context.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                            } else {
                                context.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            }
                        },
                        onControllerVisibilityChanged = { visible ->
                            controllerVisibleState = View.VISIBLE == visible
                        }
                    )


                    // 下方选项卡
                    MediaDetailTabPage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        mediaDetail = mediaDetail,
                        currentPlayItem = currentPlayUrl,
                        currentPlayList = currentPlaylist,
                        onSelectPlayList = onSelectPlayList,
                        onChangePlayItem = onChangePlayItem,
                        onSectionMediaClick = onSectionMediaClick
                    )
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
 * 视频下方选项卡
 */
@Composable
fun MediaDetailTabPage(
    modifier: Modifier,
    mediaDetail: FlexMediaDetail,
    currentPlayList: FlexMediaPlaylist?,
    currentPlayItem: FlexMediaPlaylistUrl?,
    onSelectPlayList: (FlexMediaPlaylist) -> Unit,
    onChangePlayItem: (FlexMediaPlaylist, Int) -> Unit = { _, _ -> },
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit
) {
    val updatedPlayListState by rememberUpdatedState(newValue = currentPlayList)
    val updatedPlayItemState by rememberUpdatedState(newValue = currentPlayItem)

    val pageTabs = remember {
        val pages = mediaDetail.relativeTabs.map {
            StringLabelPage(
                label = it.title,
                content = {

                }
            )
        }.toMutableList()

        // 第一页为简介
        pages.add(0, StringLabelPage(
            label = "简介",
            content = {
                MediaDetailSummaryTab(
                    mediaDetail = mediaDetail,
                    currentPlayList = updatedPlayListState,
                    currentPlayItem = updatedPlayItemState,
                    onSelectPlayList = onSelectPlayList,
                    onChangePlayItem = onChangePlayItem,
                    onSectionMediaClick = onSectionMediaClick
                )
            }
        ))
        pages
    }

    TabPager(
        modifier = modifier,
        labelPages = pageTabs
    )
}
