@file:Suppress("SpellCheckingInspection")

package com.xiaoyv.flexiflix.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xiaoyv.flexiflix.common.database.collect.CollectionDao
import com.xiaoyv.flexiflix.common.database.collect.CollectionEntity
import com.xiaoyv.flexiflix.common.database.history.HistoryDao
import com.xiaoyv.flexiflix.common.database.history.HistoryEntity

@Database(
    entities = [CollectionEntity::class, HistoryEntity::class],
    version = LocalDatabase.DATABASE_VERSION
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun collectionDao(): CollectionDao

    abstract fun historyDao(): HistoryDao

    companion object {
        const val DATABASE_VERSION = 6

        const val DATABASE_NAME = "flexiflix"
        const val TABLE_NAME_COLLECT = "collect"
        const val TABLE_NAME_HISTORY = "history"
    }
}
