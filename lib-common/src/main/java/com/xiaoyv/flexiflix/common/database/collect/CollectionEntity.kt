package com.xiaoyv.flexiflix.common.database.collect

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xiaoyv.flexiflix.common.database.LocalDatabase
import kotlinx.parcelize.Parcelize

/**
 * 收藏的本地储存库
 */
@Keep
@Parcelize
@Entity(tableName = LocalDatabase.TABLE_NAME_COLLECT)
data class CollectionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") var id: Long = 0,
    @ColumnInfo("type") val type: Int = 0,

    @ColumnInfo("sourceId") val sourceId: String,
    @ColumnInfo("mediaId") val mediaId: String,
    @ColumnInfo("title") var title: String,
    @ColumnInfo("description") var description: String,
    @ColumnInfo("cover") var cover: String,
    @ColumnInfo("url") var url: String,
    @ColumnInfo("playCount") var playCount: String? = null,
    @ColumnInfo("createAt") var createAt: String? = null,
    @ColumnInfo("publisher") var publisher: String? = null,
    @ColumnInfo("playlist") var playlistCount: Int = 0,
    @ColumnInfo("series") var seriesCount: Int = 0,
    @ColumnInfo("tags") var tags: String? = null,
    @ColumnInfo("saveAt") val saveAt: Long = System.currentTimeMillis(),
) : Parcelable
