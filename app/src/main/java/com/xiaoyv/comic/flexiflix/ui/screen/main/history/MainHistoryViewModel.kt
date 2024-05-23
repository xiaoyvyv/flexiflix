package com.xiaoyv.comic.flexiflix.ui.screen.main.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.xiaoyv.comic.flexiflix.data.database.DatabaseRepositoryImpl
import com.xiaoyv.flexiflix.common.utils.defaultPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [MainHistoryViewModel]
 *
 * @author why
 * @since 5/20/24
 */
@HiltViewModel
class MainHistoryViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val localRepositoryImpl: DatabaseRepositoryImpl,
) : ViewModel() {

    val pageSource = defaultPaging {
        localRepositoryImpl.queryCollectsByPaging()
    }
}
