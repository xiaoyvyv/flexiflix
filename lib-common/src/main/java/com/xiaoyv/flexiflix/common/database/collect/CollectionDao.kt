package com.xiaoyv.flexiflix.common.database.collect

import androidx.annotation.Keep
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.xiaoyv.flexiflix.common.database.LocalDatabase

@Keep
@Dao
interface CollectionDao {
    @Query("SELECT * FROM ${LocalDatabase.TABLE_NAME_COLLECT} ORDER BY saveAt DESC LIMIT :offset, :size")
    fun queryByPage(offset: Int, size: Int): List<CollectionEntity>

    @Query("SELECT * FROM ${LocalDatabase.TABLE_NAME_COLLECT} WHERE sourceId = :sourceId ORDER BY saveAt DESC LIMIT :offset, :size")
    fun queryByPage(sourceId: String, offset: Int, size: Int): List<CollectionEntity>

    @Query("SELECT COUNT(*) FROM ${LocalDatabase.TABLE_NAME_COLLECT} WHERE sourceId = :sourceId AND mediaId = :mediaId")
    fun isCollected(sourceId: String, mediaId: String): Boolean

    @Insert
    fun insertAll(vararg users: CollectionEntity)

    @Delete
    fun delete(vararg users: CollectionEntity)

    /**
     * 根据 `sourceId` 和 `mediaId` 删除
     */
    @Query("DELETE FROM ${LocalDatabase.TABLE_NAME_COLLECT} WHERE sourceId = :sourceId AND mediaId = :mediaId")
    fun delete(sourceId: String, mediaId: String)

    /**
     * 删除全部
     */
    @Query("DELETE FROM ${LocalDatabase.TABLE_NAME_COLLECT}")
    fun deleteAll()
}
