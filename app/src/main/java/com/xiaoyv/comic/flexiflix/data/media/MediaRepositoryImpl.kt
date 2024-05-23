package com.xiaoyv.comic.flexiflix.data.media

import androidx.paging.PagingSource
import com.xiaoyv.comic.flexiflix.data.extension.ExtensionRepository
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.common.utils.defaultPagingSource
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylistUrl
import com.xiaoyv.flexiflix.extension.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.model.FlexSearchOption
import com.xiaoyv.flexiflix.extension.model.FlexSearchOptionItem
import com.xiaoyv.flexiflix.extension.utils.runCatchingPrint
import com.xiaoyv.flexiflix.extension.utils.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * [MediaRepositoryImpl]
 *
 * @author why
 * @since 5/9/24
 */
@Singleton
class MediaRepositoryImpl @Inject constructor(
    private val extensionRepository: ExtensionRepository,
) : MediaRepository {

    override fun searchSource(
        sourceId: String,
        keyword: String,
        queryMap: Map<String, String>,
    ): PagingSource<Int, FlexMediaSectionItem> {
        return defaultPagingSource { current, size ->
            extensionRepository
                .getExtensionById(sourceId).getOrThrow()
                .fetchMediaSearchResult(
                    keyword = keyword,
                    page = current,
                    searchMap = queryMap
                ).getOrThrow()
        }
    }

    override fun sectionSource(
        sourceId: String,
        section: FlexMediaSection,
    ): PagingSource<Int, FlexMediaSectionItem> {
        return defaultPagingSource { current, size ->
            extensionRepository
                .getExtensionById(sourceId).getOrThrow()
                .fetchSectionMediaPages(
                    sectionId = section.id,
                    sectionExtras = section.extras.orEmpty(),
                    page = current,
                ).getOrThrow()
        }
    }

    override suspend fun getHomeSections(sourceId: String): Result<List<FlexMediaSection>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                extensionRepository
                    .getExtensionById(sourceId).getOrThrow()
                    .fetchHomeSections().getOrThrow()
            }
        }
    }

    override suspend fun getMediaDetail(
        sourceId: String,
        mediaId: String,
    ): Result<FlexMediaDetail> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                extensionRepository
                    .getExtensionById(sourceId).getOrThrow()
                    .fetchMediaDetail(mediaId, emptyMap()).getOrThrow()
            }
        }
    }

    override suspend fun getMediaRawUrl(
        sourceId: String,
        playlistUrl: FlexMediaPlaylistUrl,
    ): Result<FlexMediaPlaylistUrl> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                extensionRepository
                    .getExtensionById(sourceId).getOrThrow()
                    .fetchMediaRawUrl(playlistUrl).getOrThrow()
            }
        }
    }

    override suspend fun getMediaSearchOption(sourceId: String): Result<FlexSearchOption> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                extensionRepository
                    .getExtensionById(sourceId).getOrThrow()
                    .fetchMediaSearchConfig().getOrThrow()
            }
        }
    }


    override suspend fun getSectionMediaFilter(
        sourceId: String,
        section: FlexMediaSection,
    ): Result<List<FlexSearchOptionItem>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                extensionRepository
                    .getExtensionById(sourceId).getOrThrow()
                    .fetchSectionMediaFilter(section).getOrThrow()
            }
        }
    }


}