package com.xiaoyv.comic.flexiflix.ui.screen.feature.detail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.flexiflix.data.database.DatabaseRepository
import com.xiaoyv.comic.flexiflix.data.media.MediaRepository
import com.xiaoyv.flexiflix.common.database.history.HistoryEntity
import com.xiaoyv.flexiflix.common.model.StateContent
import com.xiaoyv.flexiflix.common.model.hasData
import com.xiaoyv.flexiflix.common.model.payload
import com.xiaoyv.flexiflix.common.utils.errMsg
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import com.xiaoyv.flexiflix.common.utils.toast
import com.xiaoyv.flexiflix.extension.MediaSourceType
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
    val uiState = _uiState.asStateFlow()

    private val _currentPlayUrl = mutableStateFlowOf<FlexMediaPlaylistUrl?>(null)
    val currentPlayUrl = _currentPlayUrl.asStateFlow()

    private val _currentPlaylist = mutableStateFlowOf<FlexMediaPlaylist?>(null)
    val currentPlaylist = _currentPlaylist.asStateFlow()

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

            // 恢复播放记录
            if (state.data.hasData) {
                restorePlayHistory(state.data.payload())
            }

            _uiState.update { state }
        }
    }

    /**
     * 重试先延迟一下
     */
    fun retry() {
        refresh(300)
    }

    /**
     * 恢复播放记录
     *
     * 默认播放选中第一个播放列表的第一个数据
     */
    private suspend fun restorePlayHistory(mediaDetail: FlexMediaDetail) {
        val playlists = mediaDetail.playlist.orEmpty()

        // 查询浏览历史
        val history = databaseRepository.queryHistoryById(args.sourceId, args.mediaId).getOrNull()
        val historyPlaylist = history?.playlist.orEmpty()
        val historyPlaylistItemId = history?.playlistItemId.orEmpty()

        // 恢复历史记录播放列表和章节
        if (playlists.isNotEmpty()) {
            val playlist = playlists.find { it.title == historyPlaylist } ?: playlists.first()
            if (playlist.items.isNotEmpty()) {
                val lastPlaylistItemIndex = playlist.items
                    .indexOfFirst { it.id == historyPlaylistItemId }
                    .coerceAtLeast(0)

                selectPlayListItem(playlist, lastPlaylistItemIndex)
            }
        }
    }

    /**
     * 切换播放列表
     */
    fun selectPlayList(playlist: FlexMediaPlaylist) {
        _currentPlaylist.update { playlist.copy() }
    }

    /**
     * 切换播放列表下的播放数据
     */
    suspend fun selectPlayListItem(playlist: FlexMediaPlaylist, index: Int) {
        selectPlayList(playlist)

        val playUrl = playlist.items.getOrNull(index) ?: return
        val urlResult = if (playUrl.needLoadRawUrlById) {
            mediaRepository.getMediaRawUrl(args.sourceId, playUrl)
        } else {
            Result.success(playUrl)
        }

        // 出错了
        val error = urlResult.exceptionOrNull()
        if (error != null) {
            context.toast(error.errMsg)
        }

        // 切换播放
        val playlistUrl = urlResult.getOrNull()
        if (playlistUrl != null) {
            _currentPlayUrl.update { playlistUrl }

            // 保存历史
            saveHistory(playlist, playlistUrl)
        }
    }


    /**
     * 保存历史
     */
    private suspend fun saveHistory(
        playlist: FlexMediaPlaylist,
        playlistUrl: FlexMediaPlaylistUrl,
    ) {
        if (uiState.value.data.hasData) {
            val payload = uiState.value.data.payload()

            val entity = HistoryEntity(
                type = MediaSourceType.TYPE_JVM,
                sourceId = args.sourceId,
                mediaId = payload.id,
                title = payload.title,
                description = payload.description,
                cover = payload.cover,
                url = payload.url,
                playlist = playlist.title,
                playlistItemId = playlistUrl.id
            )

            // 保存
            databaseRepository.saveHistories(entity)
        }
    }
}