package com.xiaoyv.flexiflix.common.database.history

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xiaoyv.flexiflix.common.database.LocalDatabase
import com.xiaoyv.flexiflix.extension.MediaSourceType
import kotlinx.parcelize.Parcelize

/**
 * 历史浏览库
 */
@Keep
@Parcelize
@Entity(tableName = LocalDatabase.TABLE_NAME_HISTORY)
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") var id: Long = 0,
    @ColumnInfo("type") @MediaSourceType val type: Int = MediaSourceType.TYPE_UNKNOWN,

    @ColumnInfo("sourceId") val sourceId: String,
    @ColumnInfo("mediaId") val mediaId: String,
    @ColumnInfo("title") var title: String,
    @ColumnInfo("description") var description: String,
    @ColumnInfo("cover") var cover: String,
    @ColumnInfo("url") var url: String,
    @ColumnInfo("playlist") var playlist: String,
    @ColumnInfo("playlistItemId") var playlistItemId: String,
    @ColumnInfo("saveAt") val saveAt: Long = System.currentTimeMillis(),
) : Parcelable
