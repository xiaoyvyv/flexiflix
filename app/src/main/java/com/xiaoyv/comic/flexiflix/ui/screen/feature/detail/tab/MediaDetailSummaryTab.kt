package com.xiaoyv.comic.flexiflix.ui.screen.feature.detail.tab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.GlideImage
import com.xiaoyv.comic.flexiflix.ui.component.ContainerCard
import com.xiaoyv.comic.flexiflix.ui.component.ElevatedCardWrap
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaDetailTab
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaPlaylist
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaPlaylistUrl
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaTag
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaUser
import com.xiaoyv.flexiflix.extension.java.utils.UNKNOWN_LONG
import com.xiaoyv.flexiflix.extension.java.utils.UNKNOWN_STRING

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
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit = {}
) {
    Column(
        modifier = Modifier
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

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mediaDetail.playCount.isNotBlank()) {
                Text(
                    text = mediaDetail.playCount,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = mediaDetail.createAt,
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = mediaDetail.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 6,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(mediaDetail.playlist) {
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
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = CardDefaults.shape,
                        color = CardDefaults.cardColors().containerColor
                    )
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy((-12).dp)
            ) {
                currentPlayList.items.forEachIndexed { index, url ->
                    // 选中
                    if (currentPlayItem?.id == url.id) {
                        Card(
                            colors = CardDefaults.elevatedCardColors().copy(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            onClick = { onChangePlayItem(currentPlayList, index) }) {
                            Text(
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                text = url.title,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    // 未选中
                    else {
                        OutlinedCard(onClick = { onChangePlayItem(currentPlayList, index) }) {
                            Text(
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                text = url.title,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }


        /*
                ConstraintLayout(modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(info) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(playCount.bottom, 8.dp)
                        width = Dimension.fillToConstraints
                    }
                ) {
                    val (avatar, author, type, description) = createRefs()

                    ElevatedImage(
                        modifier = Modifier
                            .size(44.dp)
                            .constrainAs(avatar) {
                                top.linkTo(parent.top, 12.dp)
                                start.linkTo(parent.start, 12.dp)
                            },
                        model = mediaDetail.publisher.avatar,
                    )

                    Text(
                        modifier = Modifier.constrainAs(author) {
                            top.linkTo(avatar.top)
                            start.linkTo(avatar.end, 8.dp)
                            end.linkTo(parent.end, 12.dp)
                            width = Dimension.fillToConstraints
                        },
                        text = mediaDetail.publisher.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier.constrainAs(type) {
                            start.linkTo(avatar.end, 8.dp)
                            end.linkTo(parent.end, 12.dp)
                            bottom.linkTo(avatar.bottom)
                            width = Dimension.fillToConstraints
                        },
                        text = mediaDetail.type,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }*/

        // https://top.1080pzy.co/202311/30/xR6MwizRs73/video/index.m3u8
        // 播放系列
        /*        if (mediaDetail.series.isNotEmpty()) {
                    ContainerCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(series) {
                                start.linkTo(parent.start, 16.dp)
                                end.linkTo(parent.end, 16.dp)
                                top.linkTo(description.bottom, 16.dp)
                                width = Dimension.fillToConstraints
                            },
                        onClick = {},
                    ) {
                        MediaDetailSummarySeries(
                            modifier = Modifier.fillMaxWidth(),
                            mediaDetail = mediaDetail,
                            onSectionMediaClick = onSectionMediaClick
                        )
                    }
                }*/

        /* ContainerCard(
             modifier = Modifier.constrainAs(tags) {
                 start.linkTo(parent.start, 16.dp)
                 end.linkTo(parent.end, 16.dp)
                 top.linkTo(series.bottom, 16.dp)
                 bottom.linkTo(parent.bottom, 40.dp)
                 width = Dimension.fillToConstraints
             },
             onClick = {}
         ) {
             Column {
                 Text(
                     modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 8.dp),
                     text = "TAGs",
                     style = MaterialTheme.typography.titleLarge,
                     color = MaterialTheme.colorScheme.onSurface,
                     fontWeight = FontWeight.SemiBold
                 )

                 FlowRow(
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(start = 12.dp, bottom = 8.dp),
                     horizontalArrangement = Arrangement.Start
                 ) {
                     mediaDetail.tags.forEach {
                         ElevatedSuggestionChip(
                             modifier = Modifier.padding(end = 12.dp),
                             onClick = {

                             },
                             label = {
                                 Text(text = it.name)
                             }
                         )
                     }
                 }
             }
         }*/
    }
}

@Composable
fun MediaDetailSummarySeries(
    modifier: Modifier,
    mediaDetail: FlexMediaDetail,
    onSectionMediaClick: (FlexMediaSectionItem) -> Unit
) {
    val gradientColors = remember {
        listOf(
            Color.Black.copy(alpha = 0f),
            Color.Black.copy(alpha = 0.1f),
            Color.Black.copy(alpha = 0.9f),
        )
    }

    val series = mediaDetail.series.maxBy { it.items.size }

    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 12.dp, bottom = 8.dp),
            text = "系列 ${series.count} 条",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, end = 6.dp, top = 6.dp, bottom = 12.dp)
        ) {
            items(series.items) { media ->
                Column(
                    modifier = Modifier
                        .width(185.dp)
                        .padding(horizontal = 6.dp)
                ) {
                    ContainerCard(onClick = { onSectionMediaClick(media) }) {
                        Box(Modifier.fillMaxWidth()) {
                            GlideImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(media.layout.aspectRatio)
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
                                model = media.cover,
                                contentDescription = null,
                                transition = CrossFade,
                                contentScale = ContentScale.Crop
                            )

                            Text(
                                modifier = Modifier
                                    .padding(bottom = 6.dp, start = 6.dp)
                                    .align(Alignment.BottomStart),
                                text = media.overlay.bottomStart,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )

                            Text(
                                modifier = Modifier
                                    .padding(bottom = 6.dp, end = 6.dp)
                                    .align(Alignment.BottomEnd),
                                text = media.overlay.bottomEnd,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                        }
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
                                id = "",
                                mediaUrl = "",
                                title = "第 $it 集"
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
    AppTheme {
        MediaDetailSummaryTab(
            onSectionMediaClick = {},
            currentPlayList = playlist.firstOrNull(),
            currentPlayItem = null,
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
                series = emptyList(),
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
