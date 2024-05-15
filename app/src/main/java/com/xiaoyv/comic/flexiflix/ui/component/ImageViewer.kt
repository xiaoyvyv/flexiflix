package com.xiaoyv.comic.reader.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import com.xiaoyv.comic.flexiflix.ui.component.Loading
import com.xiaoyv.flexiflix.common.utils.addStateListener

/**
 * [ImageViewer]
 *
 * @author why
 * @since 5/2/24
 */
@Composable
fun ImageViewer(
    modifier: Modifier = Modifier,
    item: Any?,
    current: Int = 0,
    onImageTap: (Offset) -> Unit = {}
) {
    ImageViewer(
        modifier = modifier,
        items = if (item is List<*>) item else listOf(item),
        current = current,
        onImageTap = onImageTap
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageViewer(
    modifier: Modifier = Modifier,
    items: List<Any?>?,
    current: Int = 0,
    onImageTap: (Offset) -> Unit = {}
) {
    val pagerState = rememberPagerState { items?.size ?: 0 }
    Box(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black),
            state = pagerState
        ) {
            val loading = remember { mutableStateOf(true) }

            Box {
                GlideImage(
                    modifier = Modifier.fillMaxSize(),
                    model = items?.get(it) ?: "",
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    requestBuilderTransform = {
                        it.addStateListener(loading)
                    }
                )

                if (loading.value) {
                    Loading()
                }
            }
        }

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}