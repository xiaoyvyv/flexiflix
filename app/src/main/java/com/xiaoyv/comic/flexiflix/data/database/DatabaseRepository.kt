package com.xiaoyv.comic.flexiflix.data.database

import androidx.paging.PagingSource
import com.xiaoyv.flexiflix.common.database.collect.CollectionEntity
import com.xiaoyv.flexiflix.common.database.history.HistoryEntity

/**
 * [DatabaseRepository]
 *
 * @author why
 * @since 5/20/24
 */
interface DatabaseRepository {
    /**
     * 收藏相关
     */
    fun queryCollectionByPaging(): PagingSource<Int, CollectionEntity>

    suspend fun queryCollections(page: Int, size: Int): Result<List<CollectionEntity>>

    suspend fun saveCollections(vararg collections: CollectionEntity): Result<Unit>

    suspend fun deleteCollections(vararg collections: CollectionEntity): Result<Unit>

    /**
     * 浏览历史相关
     */
    fun queryHistoryByPaging(): PagingSource<Int, HistoryEntity>

    suspend fun queryHistoryById(sourceId: String, mediaId: String): Result<HistoryEntity>

    suspend fun queryHistories(page: Int, size: Int): Result<List<HistoryEntity>>

    suspend fun saveHistories(vararg histories: HistoryEntity): Result<Unit>

    suspend fun deleteHistories(vararg histories: HistoryEntity): Result<Unit>
}