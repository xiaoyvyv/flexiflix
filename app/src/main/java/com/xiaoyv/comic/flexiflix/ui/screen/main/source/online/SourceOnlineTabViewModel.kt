package com.xiaoyv.comic.flexiflix.ui.screen.main.source.online

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiaoyv.comic.flexiflix.data.extension.ExtensionRepository
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.extension.utils.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [SourceOnlineTabViewModel]
 *
 * @author why
 * @since 5/10/24
 */
@HiltViewModel
class SourceOnlineTabViewModel @Inject constructor(
    private val extensionRepository: ExtensionRepository,
) : ViewModel() {

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            val extensions = extensionRepository.getOnlineExtensions()
                .map {
                    it
                }
                .getOrElse {
                    emptyList()
                }

            debugLog { "extensions: ${extensions.toJson(true)}" }
        }
    }
}