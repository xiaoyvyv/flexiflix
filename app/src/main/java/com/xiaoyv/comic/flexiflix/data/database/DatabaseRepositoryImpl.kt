package com.xiaoyv.comic.flexiflix.data.database

import androidx.paging.PagingSource
import com.xiaoyv.flexiflix.common.database.collect.CollectionDao
import com.xiaoyv.flexiflix.common.database.collect.CollectionEntity
import com.xiaoyv.flexiflix.common.database.history.HistoryDao
import com.xiaoyv.flexiflix.common.database.history.HistoryEntity
import com.xiaoyv.flexiflix.common.utils.defaultPagingSource
import com.xiaoyv.flexiflix.extension.utils.runCatchingPrint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [DatabaseRepositoryImpl]
 *
 * @author why
 * @since 5/20/24
 */
@Singleton
class DatabaseRepositoryImpl @Inject constructor(
    private val collectionDao: CollectionDao,
    private val historyDao: HistoryDao,
) : DatabaseRepository {

    override fun queryCollectionByPaging(): PagingSource<Int, CollectionEntity> {
        return defaultPagingSource { current, size ->
            queryCollections(current, size).getOrThrow()
        }
    }


    override suspend fun queryCollections(page: Int, size: Int): Result<List<CollectionEntity>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                val offset = (page - 1).coerceAtLeast(0) * size
                collectionDao.queryByPage(offset, size)
            }
        }
    }

    override suspend fun saveCollections(vararg collections: CollectionEntity): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                // 先删除再添加
                collections.forEach { collectionDao.delete(it.sourceId, it.mediaId) }

                collectionDao.insertAll(*collections)
            }
        }
    }

    override suspend fun deleteCollections(vararg collections: CollectionEntity): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                collectionDao.delete(*collections)
            }
        }
    }

    override fun queryHistoryByPaging(): PagingSource<Int, HistoryEntity> {
        return defaultPagingSource { current, size ->
            queryHistories(current, size).getOrThrow()
        }
    }

    override suspend fun queryHistories(page: Int, size: Int): Result<List<HistoryEntity>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                val offset = (page - 1).coerceAtLeast(0) * size
                historyDao.queryByPage(offset, size)
            }
        }
    }

    override suspend fun queryHistoryById(
        sourceId: String,
        mediaId: String,
    ): Result<HistoryEntity> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                historyDao.queryById(sourceId, mediaId)
            }
        }
    }

    override suspend fun saveHistories(vararg histories: HistoryEntity): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                // 先删除再添加
                histories.forEach { historyDao.delete(it.sourceId, it.mediaId) }

                historyDao.insertAll(*histories)
            }
        }
    }

    override suspend fun deleteHistories(vararg histories: HistoryEntity): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                historyDao.delete(*histories)
            }
        }
    }
}