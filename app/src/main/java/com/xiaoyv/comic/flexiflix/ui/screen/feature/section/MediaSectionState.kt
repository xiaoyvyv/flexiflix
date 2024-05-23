package com.xiaoyv.comic.flexiflix.ui.screen.feature.section

import androidx.paging.LoadState
import com.xiaoyv.flexiflix.common.model.StateContent
import com.xiaoyv.flexiflix.extension.model.FlexKeyValue
import com.xiaoyv.flexiflix.extension.model.FlexSearchOptionItem

/**
 * [MediaSectionState]
 *
 * @author why
 * @since 5/9/24
 */
data class MediaSectionState(
    val title: String,
    val loadState: LoadState = LoadState.Loading,
    val data: StateContent<List<FlexSearchOptionItem>> = StateContent.Idle(),
)
