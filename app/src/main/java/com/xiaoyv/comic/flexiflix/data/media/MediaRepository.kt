package com.xiaoyv.comic.flexiflix.data.media

import androidx.paging.PagingSource
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylistUrl
import com.xiaoyv.flexiflix.extension.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.model.FlexSearchOption
import com.xiaoyv.flexiflix.extension.model.FlexSearchOptionItem

/**
 * [MediaRepository]
 *
 * @author why
 * @since 5/9/24
 */
interface MediaRepository {

    /**
     * 搜索数据
     */
    fun searchSource(
        sourceId: String,
        keyword: String,
        queryMap: Map<String, String>,
    ): PagingSource<Int, FlexMediaSectionItem>

    fun sectionSource(
        sourceId: String,
        section: FlexMediaSection,
    ): PagingSource<Int, FlexMediaSectionItem>

    suspend fun getHomeSections(sourceId: String): Result<List<FlexMediaSection>>

    suspend fun getMediaDetail(sourceId: String, mediaId: String): Result<FlexMediaDetail>

    suspend fun getMediaRawUrl(
        sourceId: String,
        playlistUrl: FlexMediaPlaylistUrl,
    ): Result<FlexMediaPlaylistUrl>

    suspend fun getMediaSearchOption(sourceId: String): Result<FlexSearchOption>

    suspend fun getSectionMediaFilter(
        sourceId: String,
        section: FlexMediaSection,
    ): Result<List<FlexSearchOptionItem>>
}