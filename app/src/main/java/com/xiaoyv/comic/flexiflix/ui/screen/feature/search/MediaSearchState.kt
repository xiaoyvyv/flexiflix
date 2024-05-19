package com.xiaoyv.comic.flexiflix.ui.screen.feature.search

import androidx.paging.LoadState
import com.xiaoyv.flexiflix.common.model.StateContent
import com.xiaoyv.flexiflix.extension.model.FlexSearchOption

/**
 * [MediaSearchState]
 *
 * @author why
 * @since 5/9/24
 */
data class MediaSearchState(
    val loadState: LoadState = LoadState.Loading,
    val data: StateContent<FlexSearchOption> = StateContent.Idle()
)
