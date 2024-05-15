package com.xiaoyv.comic.flexiflix.data.media

import androidx.paging.PagingSource
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaPlaylistUrl
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaSection

/**
 * [MediaRepository]
 *
 * @author why
 * @since 5/9/24
 */
interface MediaRepository {
    fun pageSource(sourceId: String): PagingSource<Int, FlexMediaSection>

    suspend fun getHomeSections(sourceId: String): Result<List<FlexMediaSection>>

    suspend fun getMediaDetail(sourceId: String, mediaId: String): Result<FlexMediaDetail>

    suspend fun getMediaRawUrl(
        sourceId: String,
        playlistUrl: FlexMediaPlaylistUrl
    ): Result<FlexMediaPlaylistUrl>
}