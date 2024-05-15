package com.xiaoyv.comic.flexiflix.ui.screen.main.source.installed

import androidx.paging.LoadState
import com.xiaoyv.comic.flexiflix.model.InstalledMediaSource
import com.xiaoyv.flexiflix.common.model.StateContent

/**
 * [SourceInstalledTabState]
 *
 * @author why
 * @since 5/10/24
 */
data class SourceInstalledTabState(
    val loadState: LoadState = LoadState.Loading,
    val data: StateContent<List<InstalledMediaSource>> = StateContent.Idle()
)