package com.xiaoyv.comic.flexiflix.ui.screen.feature.home

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.flexiflix.data.media.MediaRepository
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import com.xiaoyv.flexiflix.extension.utils.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

/**
 * [MediaHomeViewModel]
 *
 * @author why
 * @since 5/8/24
 */
@HiltViewModel
class MediaHomeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository,
) : ViewModel() {

    val args = MediaHomeArgument(savedStateHandle)

    private val _uiState = mutableStateFlowOf(MediaHomeState())
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh(delay: Long = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(loadState = LoadState.Loading) }

            if (delay != 0L) withContext(Dispatchers.IO) { delay(delay) }

            val state = mediaRepository.getHomeSections(args.sourceId)
                .map {
                    MediaHomeState(
                        loadState = LoadState.NotLoading(true),
                        items = it
                    )
                }
                .getOrElse {
                    // 403 屏蔽，手动过校验
                    if (it is HttpException && it.code() == 403) {
                        val verifyUrl = it.response()?.raw()?.request?.url?.toString()
                        MediaHomeState(loadState = LoadState.Error(it), needVerifyUrl = verifyUrl)
                    } else {
                        MediaHomeState(loadState = LoadState.Error(it))
                    }
                }

            debugLog { state.items.toJson() }

            _uiState.update { state }
        }
    }

    fun retry() {
        refresh(300)
    }

    fun needVerifyUrlShown() {
        _uiState.update { it.copy(needVerifyUrl = null) }
    }
}