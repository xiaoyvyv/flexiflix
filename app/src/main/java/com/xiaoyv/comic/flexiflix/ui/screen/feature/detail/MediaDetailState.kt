package com.xiaoyv.comic.flexiflix.ui.screen.feature.detail

import androidx.paging.LoadState
import com.xiaoyv.flexiflix.common.model.StateContent
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail

/**
 * [MediaDetailState]
 *
 * @author why
 * @since 5/9/24
 */
data class MediaDetailState(
    val loadState: LoadState = LoadState.Loading,
    val data: StateContent<FlexMediaDetail> = StateContent.Idle()
)


