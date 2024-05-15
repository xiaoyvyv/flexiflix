package com.xiaoyv.comic.flexiflix.ui.screen.feature.home

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.flexiflix.data.media.MediaRepository
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.common.utils.defaultPaging
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import com.xiaoyv.flexiflix.extension.java.MediaSourceFactory
import com.xiaoyv.flexiflix.extension.java.utils.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
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
    private val mediaRepository: MediaRepository
) : ViewModel() {

    val args = MediaHomeArgument(savedStateHandle)

    private val _uiState = mutableStateFlowOf(MediaHomeState())
    val uiState = _uiState.asStateFlow()

    /**
     * 分页数据源
     */
    val pageSource = defaultPaging {
        mediaRepository.pageSource(args.sourceId)
    }

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(loadState = LoadState.Loading) }

            val state = mediaRepository.getHomeSections(args.sourceId)
                .map {
                    MediaHomeState(
                        loadState = LoadState.NotLoading(true),
                        items = it
                    )
                }
                .getOrElse {
                    MediaHomeState(loadState = LoadState.Error(it))
                }

            debugLog { state.items.toJson() }

            _uiState.update { state }
        }
    }
}