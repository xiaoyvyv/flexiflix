package com.xiaoyv.comic.flexiflix.ui.screen.main.source.installed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.xiaoyv.comic.flexiflix.model.InstalledMediaSource
import com.xiaoyv.comic.flexiflix.ui.component.ElevatedCardWrap
import com.xiaoyv.comic.flexiflix.ui.component.LazyList
import com.xiaoyv.comic.flexiflix.ui.component.PageStateScreen
import com.xiaoyv.flexiflix.common.model.payload
import com.xiaoyv.flexiflix.common.model.asSinglePage
import com.xiaoyv.flexiflix.extension.MediaSourceType

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
        itemCount = { uiState.data.asSinglePage()}
    ) {
        LazyList(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            items(uiState.data.payload()) {
                SourceInstalledTabScreenItem(
                    source = it,
                    onSourceClick = onSourceClick
                )
            }
        }
    }
}

@Composable
fun SourceInstalledTabScreenItem(
    source: InstalledMediaSource,
    onSourceClick: (InstalledMediaSource) -> Unit = {}
) {
    ElevatedCardWrap(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = { onSourceClick(source) }
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (icon, name, type, desc, date) = createRefs()

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
                    model = source.extensionIcon,
                    contentDescription = null,
                    transition = CrossFade,
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                modifier = Modifier.constrainAs(name) {
                    top.linkTo(icon.top)
                    end.linkTo(type.start, 6.dp)
                    start.linkTo(icon.end, 12.dp)
                    width = Dimension.fillToConstraints
                },
                text = source.extensionName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                    .constrainAs(type) {
                        top.linkTo(parent.top, 8.dp)
                        end.linkTo(parent.end, 12.dp)
                    },
                text = MediaSourceType.toText(source.type),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
            )

            Text(
                modifier = Modifier.constrainAs(desc) {
                    bottom.linkTo(icon.bottom)
                    start.linkTo(icon.end, 12.dp)
                },
                text = String.format("内含 %d 个源", source.sources.size),
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
                text = source.created,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@Preview(widthDp = 411, heightDp = 300)
fun PreviewSourceInstalledTabScreenItem() {
    SourceInstalledTabScreenItem(
        source = InstalledMediaSource(
            created = "",
            type = MediaSourceType.TYPE_JVM,
            extensionPath = "",
            extensionName = "测试预览名称，测试预览名称，测试预览名称，测试预览名称",
            extensionIcon = "",
            sources = emptyList()
        )
    )
}
