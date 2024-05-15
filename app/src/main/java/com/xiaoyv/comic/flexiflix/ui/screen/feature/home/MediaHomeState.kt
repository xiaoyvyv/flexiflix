package com.xiaoyv.comic.flexiflix.ui.screen.feature.home

import androidx.paging.LoadState
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaSection

/**
 * [MediaHomeState]
 *
 * @author why
 * @since 5/9/24
 */
data class MediaHomeState(
    val loadState: LoadState = LoadState.Loading,
    val items: List<FlexMediaSection> = emptyList()
)
