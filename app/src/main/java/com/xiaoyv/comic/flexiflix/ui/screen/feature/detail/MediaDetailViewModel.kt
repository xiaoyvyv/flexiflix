package com.xiaoyv.comic.flexiflix.ui.screen.feature.detail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.flexiflix.data.database.DatabaseRepository
import com.xiaoyv.comic.flexiflix.data.media.MediaRepository
import com.xiaoyv.flexiflix.common.database.collect.CollectionEntity
import com.xiaoyv.flexiflix.common.model.StateContent
import com.xiaoyv.flexiflix.common.model.hasData
import com.xiaoyv.flexiflix.common.model.payload
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.common.utils.errMsg
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import com.xiaoyv.flexiflix.common.utils.toast
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylist
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylistUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * [MediaDetailViewModel]
 *
 * @author why
 * @since 5/9/24
 */
@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository,
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {
    val args = MediaDetailArgument(savedStateHandle)

    private val _uiState = mutableStateFlowOf(MediaDetailState())
    val uiState get() = _uiState.asStateFlow()

    private val _currentPlayUrl = mutableStateFlowOf<FlexMediaPlaylistUrl?>(null)
    val currentPlayUrl get() = _currentPlayUrl.asStateFlow()

    private val _currentPlaylist = mutableStateFlowOf<FlexMediaPlaylist?>(null)
    val currentPlaylist get() = _currentPlaylist.asStateFlow()

    init {
        refresh()
    }


    fun refresh(delay: Long = 0) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadState = LoadState.Loading) }

            if (delay != 0L) withContext(Dispatchers.IO) { delay(delay) }

            val state = mediaRepository.getMediaDetail(args.sourceId, args.mediaId)
                .map {
                    MediaDetailState(
                        loadState = LoadState.NotLoading(true),
                        data = StateContent.Payload(it)
                    )
                }.getOrElse {
                    MediaDetailState(loadState = LoadState.Error(it))
                }

            if (state.data.hasData) {
                val mediaDetail = state.data.payload()

                // 保存浏览历史
                saveHistory(mediaDetail)

                // 恢复播放记录
                restorePlayHistory(mediaDetail)
            }

            _uiState.update { state }
        }
    }

    /**
     * 恢复播放记录
     *
     * 默认播放选中第一个播放列表的第一个数据
     */
    private fun restorePlayHistory(mediaDetail: FlexMediaDetail) {
        if (mediaDetail.playlist.orEmpty().isNotEmpty()) {
            val playlist = mediaDetail.playlist.orEmpty().first()
            if (playlist.items.isNotEmpty()) {
                changePlayItem(playlist, 0)
            }
        }
    }

    /**
     * 保存历史
     */
    private fun saveHistory(payload: FlexMediaDetail) {
        viewModelScope.launch {
            val entity = CollectionEntity(
                type = 1,
                sourceId = args.sourceId,
                mediaId = payload.id,
                title = payload.title,
                description = payload.description,
                cover = payload.cover,
                url = payload.url,
                playCount = payload.playCount,
                createAt = payload.createAt,
                publisher = payload.publisher?.name,
                playlistCount = payload.playlist?.size ?: 0,
                seriesCount = payload.series?.size ?: 0,
                tags = payload.tags.orEmpty().joinToString(",") { it.name },
            )

            // 保存
            databaseRepository.saveCollections(entity)
        }
    }

    /**
     * 重试先延迟一下
     */
    fun retry() {
        refresh(300)
    }

    /**
     * 切换播放列表下的播放数据
     */
    fun changePlayItem(playlist: FlexMediaPlaylist, index: Int) {
        selectPlayList(playlist)

        viewModelScope.launch {
            val playUrl = playlist.items.getOrNull(index) ?: return@launch
            val urlResult = if (playUrl.needLoadRawUrlById) {
                mediaRepository.getMediaRawUrl(args.sourceId, playUrl)
            } else {
                Result.success(playUrl)
            }
            debugLog { "data-vid: ${urlResult.getOrNull()}" }

            // 出错了
            val error = urlResult.exceptionOrNull()
            if (error != null) {
                context.toast(error.errMsg)
            }

            _currentPlayUrl.update { urlResult.getOrNull() }
        }
    }

    /**
     * 切换播放列表
     */
    fun selectPlayList(playlist: FlexMediaPlaylist) {
        _currentPlaylist.update { playlist.copy() }
    }
}