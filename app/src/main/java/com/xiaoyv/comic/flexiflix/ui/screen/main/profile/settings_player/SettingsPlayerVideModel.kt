package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiaoyv.comic.flexiflix.ui.component.player.MediaVideoCache
import com.xiaoyv.flexiflix.common.utils.formatFileSize
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * [SettingsPlayerVideModel]
 *
 * @author why
 * @since 5/24/24
 */
@HiltViewModel
class SettingsPlayerVideModel @Inject constructor() : ViewModel() {

    private val _videoCacheSize = mutableStateFlowOf("")
    val videoCacheSize get() = _videoCacheSize.asStateFlow()

    init {
        computeCacheSize()
    }

    /**
     * 计算缓存大小
     */
    private fun computeCacheSize() {
        viewModelScope.launch {
            _videoCacheSize.update {
                withContext(Dispatchers.IO) {
                    MediaVideoCache.loadCacheSize().formatFileSize()
                }
            }
        }
    }

    /**
     * 清理缓存
     */
    fun clearCache() {
        viewModelScope.launch {
            _videoCacheSize.update {
                withContext(Dispatchers.IO) {
                    MediaVideoCache.deleteVideoCache()
                    MediaVideoCache.loadCacheSize().formatFileSize()
                }
            }
        }
    }
}