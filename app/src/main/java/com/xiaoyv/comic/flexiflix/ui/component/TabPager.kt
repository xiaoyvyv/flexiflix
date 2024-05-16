package com.xiaoyv.comic.flexiflix.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * [TabPager]
 *
 * @author why
 * @since 5/10/24
 */

@Composable
fun TabPager(
    modifier: Modifier = Modifier,
    labelPages: List<StringLabelPage>,
    onPageChange: (Int) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    Column(modifier = modifier) {
        val pagerState = rememberPagerState(pageCount = { labelPages.size })
        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = {
                Box(
                    Modifier
                        .tabIndicatorOffset(pagerState.currentPage, matchContentSize = false)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(3.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = ShapeDefaults.Small.copy(
                                bottomStart = CornerSize(0.dp),
                                bottomEnd = CornerSize(0.dp)
                            )
                        )
                )
            }
        ) {
            labelPages.forEachIndexed { index, title ->
                Tab(
                    selected = index == pagerState.currentPage,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title.label,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                )
            }
        }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                onPageChange(page)
            }
        }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
        ) { page ->
            labelPages[page].content()
        }
    }
}