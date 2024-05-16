package com.xiaoyv.comic.flexiflix.ui.screen.main.source.installed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.flexiflix.data.extension.ExtensionRepository
import com.xiaoyv.flexiflix.common.model.StateContent
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [SourceInstalledTabViewModel]
 *
 * @author why
 * @since 5/10/24
 */
@HiltViewModel
class SourceInstalledTabViewModel @Inject constructor(
    private val repository: ExtensionRepository
) : ViewModel() {

    private val _uiState = mutableStateFlowOf(SourceInstalledTabState())
    val uiState get() = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(loadState = LoadState.Loading) }

            val state = repository.getInstalledExtensions()
                .map {
                    SourceInstalledTabState(
                        loadState = LoadState.NotLoading(true),
                        data = StateContent.Payload(it)
                    )
                }
                .getOrElse {
                    SourceInstalledTabState(loadState = LoadState.Error(it))
                }

            _uiState.update { state }
        }
    }
}