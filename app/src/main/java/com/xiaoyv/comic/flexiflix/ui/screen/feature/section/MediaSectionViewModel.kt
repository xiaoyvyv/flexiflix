package com.xiaoyv.comic.flexiflix.ui.screen.feature.section

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.flexiflix.data.media.MediaRepository
import com.xiaoyv.flexiflix.common.model.StateContent
import com.xiaoyv.flexiflix.common.utils.defaultPaging
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [MediaSectionViewModel]
 *
 * @author why
 * @since 5/9/24
 */
@HiltViewModel
class MediaSectionViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository,
) : ViewModel() {
    internal val args = MediaSectionArgument(savedStateHandle)

    private val _uiState = mutableStateFlowOf(MediaSectionState(title = args.section.title))
    val uiState get() = _uiState.asStateFlow()

    val sectionSource = defaultPaging {
        mediaRepository.sectionSource(args.sourceId, args.section)
    }

    init {
        loadOptions()
    }

    private fun loadOptions() {
        viewModelScope.launch {
            _uiState.update { it.copy(loadState = LoadState.Loading) }

            val state = mediaRepository.getSectionMediaFilter(args.sourceId, args.section)
                .map {
                    MediaSectionState(
                        title = args.section.title,
                        loadState = LoadState.NotLoading(true),
                        data = StateContent.Payload(it)
                    )
                }
                .getOrElse {
                    MediaSectionState(
                        title = args.section.title,
                        loadState = LoadState.Error(it)
                    )
                }

            _uiState.update { state }
        }
    }
}
