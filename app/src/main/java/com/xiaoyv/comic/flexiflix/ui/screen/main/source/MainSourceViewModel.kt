package com.xiaoyv.comic.flexiflix.ui.screen.main.source

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiaoyv.comic.flexiflix.data.extension.ExtensionRepository
import com.xiaoyv.comic.flexiflix.model.InstalledMediaSource
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [MainSourceViewModel]
 *
 * @author why
 * @since 5/8/24
 */
@HiltViewModel
class MainSourceViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    stateHandle: SavedStateHandle,
    private val extensionRepository: ExtensionRepository
) : ViewModel() {
    private val _installState = mutableStateFlowOf<Result<InstalledMediaSource>?>(null)
    val installState = _installState.asStateFlow()

    /**
     * 安装插件
     */
    fun installExtension(extensionUri: Uri) {
        viewModelScope.launch {
            _installState.update {
                extensionRepository.installExtension(extensionUri)
            }
        }
    }

    fun clearInstallState() {
        _installState.update { null }
    }
}