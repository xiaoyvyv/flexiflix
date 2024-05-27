package com.xiaoyv.comic.flexiflix.ui.screen.feature.detail.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xiaoyv.comic.flexiflix.ui.component.PlayingAnimationBar
import com.xiaoyv.comic.flexiflix.ui.screen.feature.home.MediaHomeSectionItemRow
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetailSeries
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetailTab
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylist
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylistUrl
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.model.FlexMediaTag
import com.xiaoyv.flexiflix.extension.model.FlexMediaUser
import com.xiaoyv.flexiflix.extension.utils.UNKNOWN_LONG
import com.xiaoyv.flexiflix.extension.utils.UNKNOWN_STRING

/**
 * [MediaDetailSummaryTab]
 *
 * 简介
 */
@Composable
fun MediaDetailSummaryTab(
    mediaDetail: FlexMediaDetail,
    currentPlayList: FlexMediaPlaylist?,
    currentPlayItem: FlexMediaPlaylistUrl?,
    onSelectPlayList: (FlexMediaPlaylist) -> Unit = {},
    onChangePlayItem: (FlexMediaPlaylist, Int) -> Unit = { _, _ -> },
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(state = rememberScrollState())
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = mediaDetail.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mediaDetail.playCount.orEmpty().isNotBlank()) {
                Text(
                    text = mediaDetail.playCount.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = mediaDetail.createAt.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "简介",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = mediaDetail.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 6,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 播放列表
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(mediaDetail.playlist.orEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                        .clickable { onSelectPlayList(it) },
                    text = it.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (currentPlayList == it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        // 当前播放列表具体的章节话数等
        if (currentPlayList != null && currentPlayList.items.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(currentPlayList.items.size) { index ->
                    MediaDetailMediaItem(
                        url = currentPlayList.items[index],
                        index = index,
                        currentPlayList = currentPlayList,
                        currentPlayItem = currentPlayItem,
                        onChangePlayItem = onChangePlayItem
                    )
                }
            }
        }

        // 系列作品（推荐等）
        if (!mediaDetail.series.isNullOrEmpty()) {
            MediaDetailSummarySeries(
                mediaDetail = mediaDetail,
                onSectionMediaClick = onSectionMediaClick
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

/**
 * 系列作品
 */
@Composable
fun MediaDetailSummarySeries(
    mediaDetail: FlexMediaDetail,
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit,
) {
    mediaDetail.series?.forEach { series ->
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = series.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(12.dp))

        MediaHomeSectionItemRow(
            modifier = Modifier.fillMaxSize(),
            itemModifier = Modifier.padding(end = 16.dp),
            currentMediaId = mediaDetail.id,
            items = series.items,
            onSectionMediaClick = {
                // 同一个媒体点击不再打开
                if (mediaDetail.id != it.id) {
                    onSectionMediaClick(it)
                }
            }
        )
    }
}

@Composable
fun MediaDetailMediaItem(
    url: FlexMediaPlaylistUrl,
    index: Int,
    currentPlayList: FlexMediaPlaylist,
    currentPlayItem: FlexMediaPlaylistUrl?,
    onChangePlayItem: (FlexMediaPlaylist, Int) -> Unit = { _, _ -> },
) {
    val text = remember {
        val spit = url.title.split("-")
        spit.getOrNull(0).orEmpty().trim() to spit.getOrNull(1).orEmpty().trim()
    }

    // 选中
    val selected = currentPlayItem?.id == url.id

    Card(
        modifier = Modifier
            .width(120.dp)
            .aspectRatio(4 / 2f)
            .padding(end = 12.dp),
        shape = if (selected) CardDefaults.outlinedShape else CardDefaults.shape,
        colors = if (selected) {
            CardDefaults.outlinedCardColors()
                .copy(containerColor = MaterialTheme.colorScheme.primary)
        } else {
            CardDefaults.cardColors()
        },
        onClick = { onChangePlayItem(currentPlayList, index) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 播放中动画
                if (selected) {
                    PlayingAnimationBar(
                        sizeDp = 16f,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(2.dp))
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = text.first,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (text.second.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = text.second,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Preview(widthDp = 411, heightDp = 711)
@Composable
fun PreviewMediaDetailScreen() {
    val playlist = remember {
        listOf(
            FlexMediaPlaylist(
                title = "默认播放",
                items = mutableListOf<FlexMediaPlaylistUrl>().apply {
                    repeat(20) {
                        add(
                            FlexMediaPlaylistUrl(
                                id = it.toString(),
                                mediaUrls = emptyList(),
                                title = "第 $it 集-标题"
                            )
                        )
                    }
                }
            ),
            FlexMediaPlaylist(
                title = "专线一",
                items = emptyList()
            ),
        )
    }

    val series = mutableListOf<FlexMediaDetailSeries>().apply {
        repeat(2) {
            add(
                FlexMediaDetailSeries(
                    title = "测试",
                    mediaId = "",
                    count = 3,
                    items = listOf(
                        FlexMediaSectionItem(
                            id = "",
                            cover = "https://tx-free-imgs2.acfun.cn/kimg/bs2/zt-image-host/ChYwOGQzZWY5ZjAzMTBkYWU0OGM4ZTAxEJvM1y8.webp",
                            title = "标题",
                            description = ""
                        )
                    )
                )
            )
        }
    }

    AppTheme {
        MediaDetailSummaryTab(
            onSectionMediaClick = {},
            currentPlayList = playlist.first(),
            currentPlayItem = playlist.first().items.first(),
            mediaDetail = FlexMediaDetail(
                id = "1",
                title = "回复术士",
                description = "ゲーム実況動画の再生回数が増えず悩んでいる愛に、友人は自分のカラダを使って配信してみたらと持ちかける。\n" +
                        "再生回数は急上昇し、もっと自己顕示欲を満たしたい愛は、友人からは決してするなと言われていたオフ会まで開いてしまう。\n" +
                        "カラオケ店でワルい男にクスリを盛られ、○○してしまう愛。\n" +
                        "他に集まったファンは愛が「お持ち帰り」される様を黙って見てるしかない。\n" +
                        "ワルい男に下着姿で、ホテルで寝かされる愛…。",
                cover = "",
                url = "",
                playCount = "观看次数：12万次",
                createAt = "2024-12-09",
                type = "表番",
                duration = UNKNOWN_LONG,
                size = "120 MB",
                publisher = FlexMediaUser(
                    id = "",
                    name = "小明",
                    avatar = "",
                    role = UNKNOWN_STRING
                ),
                playlist = playlist,
                tags = listOf(
                    FlexMediaTag(id = "", name = "XXX"),
                    FlexMediaTag(id = "", name = "XXX"),
                    FlexMediaTag(id = "", name = "XXX"),
                    FlexMediaTag(id = "", name = "XXX"),
                    FlexMediaTag(id = "", name = "XXX"),
                    FlexMediaTag(id = "", name = "XXX"),
                    FlexMediaTag(id = "", name = "XXX"),
                    FlexMediaTag(id = "", name = "XXX"),
                    FlexMediaTag(id = "", name = "XXX"),
                    FlexMediaTag(id = "", name = "XXX"),
                ),
                series = series,
                relativeTabs = listOf(
                    FlexMediaDetailTab(
                        id = "r",
                        mediaId = "",
                        title = "相关影片"
                    ),
                    FlexMediaDetailTab(
                        id = "c",
                        mediaId = "",
                        title = "评论"
                    ),
                ),
            )
        )
    }
}
