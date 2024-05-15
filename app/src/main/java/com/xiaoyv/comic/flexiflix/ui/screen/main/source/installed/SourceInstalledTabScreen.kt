package com.xiaoyv.comic.flexiflix.ui.screen.main.source.installed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.GlideImage
import com.xiaoyv.comic.flexiflix.model.InstalledMediaSource
import com.xiaoyv.comic.flexiflix.ui.component.ElevatedCardWrap
import com.xiaoyv.comic.flexiflix.ui.component.LazyList
import com.xiaoyv.comic.flexiflix.ui.component.PageStateScreen
import com.xiaoyv.flexiflix.common.model.hasData
import com.xiaoyv.flexiflix.common.model.payload
import com.xiaoyv.flexiflix.common.utils.debugLog

/**
 * [SourceInstalledTabRoute]
 *
 * @author why
 * @since 5/10/24
 */

@Composable
fun SourceInstalledTabRoute(
    onSourceClick: (InstalledMediaSource) -> Unit,
    refreshListState: Any,
) {
    val viewModel = hiltViewModel<SourceInstalledTabViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 刷新安装列表
    LaunchedEffect(refreshListState) {
        viewModel.refresh()
    }

    SourceInstalledTabScreen(
        uiState = uiState,
        onSourceClick = onSourceClick
    )
}

@Composable
fun SourceInstalledTabScreen(
    uiState: SourceInstalledTabState,
    onSourceClick: (InstalledMediaSource) -> Unit
) {
    PageStateScreen(
        loadState = uiState.loadState,
        itemCount = { if (uiState.data.hasData ) 1 else 0 }
    ) {
        LazyList(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            items(uiState.data.payload()) {
                ElevatedCardWrap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = { onSourceClick(it) }
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (icon, name, desc, date, count) = createRefs()

                        ElevatedCardWrap(
                            modifier = Modifier
                                .size(44.dp)
                                .constrainAs(icon) {
                                    top.linkTo(parent.top, 12.dp)
                                    start.linkTo(parent.start, 12.dp)
                                    bottom.linkTo(parent.bottom, 12.dp)
                                }
                        ) {
                            GlideImage(
                                modifier = Modifier.fillMaxSize(),
                                model = it.extensionIcon,
                                contentDescription = null,
                                transition = CrossFade,
                                contentScale = ContentScale.Crop
                            )
                        }

                        Text(
                            modifier = Modifier.constrainAs(name) {
                                top.linkTo(icon.top)
                                end.linkTo(parent.end, 12.dp)
                                start.linkTo(icon.end, 12.dp)
                                width = Dimension.fillToConstraints
                            },
                            text = it.extensionName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            modifier = Modifier.constrainAs(desc) {
                                bottom.linkTo(icon.bottom)
                                start.linkTo(icon.end, 12.dp)
                            },
                            text = String.format("内含 %d 个源", it.sources.size),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            modifier = Modifier.constrainAs(date) {
                                bottom.linkTo(icon.bottom)
                                end.linkTo(parent.end, 12.dp)
                            },
                            text = it.created,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}