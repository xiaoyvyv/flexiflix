@file:Suppress("SpellCheckingInspection")

package com.xiaoyv.flexiflix.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xiaoyv.flexiflix.common.database.collect.CollectionEntity
import com.xiaoyv.flexiflix.common.database.collect.CollectionDao

@Database(
    entities = [CollectionEntity::class],
    version = LocalDatabase.DATABASE_VERSION
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun collectionDao(): CollectionDao

    companion object {
        const val DATABASE_VERSION = 6

        const val DATABASE_NAME = "flexiflix"
        const val TABLE_NAME_COLLECT = "collect"
    }
}
