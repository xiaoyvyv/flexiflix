@file:OptIn(ExperimentalPagingApi::class)

package com.xiaoyv.flexiflix.common.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import okio.IOException

/**
 * [defaultPagingConfig]
 *
 * @author why
 * @since 4/28/24
 */
val defaultPagingConfig
    get() = PagingConfig(pageSize = 20, initialLoadSize = 20)

/**
 * 默认分页
 */
fun <Key : Any, Value : Any> ViewModel.defaultPaging(
    config: PagingConfig = defaultPagingConfig,
    initialKey: Key? = null,
    remoteMediator: RemoteMediator<Key, Value>? = null,
    pagingSourceFactory: () -> PagingSource<Key, Value>,
): Flow<PagingData<Value>> {
    return Pager(config, initialKey, remoteMediator, pagingSourceFactory)
        .flow.cachedIn(viewModelScope)
}

/**
 * 默认按页分页模式
 */
inline fun <T : Any> defaultPagingSource(
    crossinline loadData: suspend (current: Int, size: Int) -> List<T>,
): PagingSource<Int, T> {
    return object : PagingSource<Int, T>() {
        private val firstPage = 1

        override fun getRefreshKey(state: PagingState<Int, T>): Int {
            return firstPage
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
            val current = params.key ?: firstPage
            val loadSize = params.loadSize

            return try {
                val list = loadData(current, loadSize)

                check(list.isNotEmpty()) { "No more data" }

                LoadResult.Page(
                    data = list,
                    prevKey = if (current > firstPage) current - 1 else null,
                    nextKey = if (list.isNotEmpty()) current + 1 else null
                )
            } catch (e: Throwable) {
                LoadResult.Error(if (e is IOException) e else IOException(e))
            }
        }
    }
}