package com.xiaoyv.comic.flexiflix.data.database

import androidx.paging.PagingSource
import com.xiaoyv.flexiflix.common.database.collect.CollectionDao
import com.xiaoyv.flexiflix.common.database.collect.CollectionEntity
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
) : DatabaseRepository {

    override fun queryCollectsByPaging(): PagingSource<Int, CollectionEntity> {
        return defaultPagingSource { current, size ->
            queryCollects(current, size).getOrThrow()
        }
    }


    override suspend fun queryCollects(page: Int, size: Int): Result<List<CollectionEntity>> {
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
}