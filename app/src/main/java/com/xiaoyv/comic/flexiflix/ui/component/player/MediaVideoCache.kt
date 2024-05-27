@file:OptIn(UnstableApi::class)

package com.xiaoyv.comic.flexiflix.ui.component.player

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import com.xiaoyv.comic.flexiflix.application
import com.xiaoyv.flexiflix.extension.utils.cacheDir

/**
 * [MediaVideoCache]
 *
 * @author why
 * @since 5/23/24
 */
object MediaVideoCache {
    private val cacheDir by lazy {
        cacheDir("player")
    }

    private val databaseProvider by lazy {
        StandaloneDatabaseProvider(application)
    }

    private var _simpleCache: SimpleCache? = null
    val sampleCache: SimpleCache
        get() {
            if (_simpleCache == null) {
                _simpleCache = SimpleCache(
                    cacheDir,
                    LeastRecentlyUsedCacheEvictor(500 * 1024 * 1024L),
                    databaseProvider
                )
            }
            return requireNotNull(_simpleCache)
        }

    /**
     * 递归计算文件夹大小
     */
    fun loadCacheSize(): Long {
        return cacheDir
            .walk()
            .sumOf { if (it.isFile) it.length() else 0 }
    }

    /**
     * 删除视频缓存
     */
    fun deleteVideoCache() {
        _simpleCache?.release()
        _simpleCache = null

        SimpleCache.delete(cacheDir, databaseProvider)
    }
}