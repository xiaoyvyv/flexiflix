package com.xiaoyv.flexiflix.common.database.history

import androidx.annotation.Keep
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.xiaoyv.flexiflix.common.database.LocalDatabase

@Keep
@Dao
interface HistoryDao {

    @Query("SELECT * FROM ${LocalDatabase.TABLE_NAME_HISTORY} ORDER BY saveAt DESC LIMIT :offset, :size")
    fun queryByPage(offset: Int, size: Int): List<HistoryEntity>

    @Query("SELECT * FROM ${LocalDatabase.TABLE_NAME_HISTORY} WHERE sourceId = :sourceId ORDER BY saveAt DESC LIMIT :offset, :size")
    fun queryByPage(sourceId: String, offset: Int, size: Int): List<HistoryEntity>

    @Query("SELECT * FROM ${LocalDatabase.TABLE_NAME_HISTORY} WHERE sourceId = :sourceId AND mediaId = :mediaId LIMIT 1")
    fun queryById(sourceId: String, mediaId: String): HistoryEntity

    @Query("SELECT COUNT(*) FROM ${LocalDatabase.TABLE_NAME_HISTORY} WHERE sourceId = :sourceId AND mediaId = :mediaId")
    fun isCollected(sourceId: String, mediaId: String): Boolean

    @Insert
    fun insertAll(vararg users: HistoryEntity)

    @Delete
    fun delete(vararg users: HistoryEntity)

    /**
     * 根据 `sourceId` 和 `mediaId` 删除
     */
    @Query("DELETE FROM ${LocalDatabase.TABLE_NAME_HISTORY} WHERE sourceId = :sourceId AND mediaId = :mediaId")
    fun delete(sourceId: String, mediaId: String)

    /**
     * 删除全部
     */
    @Query("DELETE FROM ${LocalDatabase.TABLE_NAME_HISTORY}")
    fun deleteAll()
}
