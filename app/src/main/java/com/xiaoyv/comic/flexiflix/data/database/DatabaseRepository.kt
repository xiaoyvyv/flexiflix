package com.xiaoyv.comic.flexiflix.data.database

import androidx.paging.PagingSource
import com.xiaoyv.flexiflix.common.database.collect.CollectionEntity
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem

/**
 * [DatabaseRepository]
 *
 * @author why
 * @since 5/20/24
 */
interface DatabaseRepository {
    fun queryCollectsByPaging(): PagingSource<Int, CollectionEntity>

    suspend fun queryCollects(page: Int, size: Int): Result<List<CollectionEntity>>

    suspend fun saveCollections(vararg collections: CollectionEntity): Result<Unit>

    suspend fun deleteCollections(vararg collections: CollectionEntity): Result<Unit>
}