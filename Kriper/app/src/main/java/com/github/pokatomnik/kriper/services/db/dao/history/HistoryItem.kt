package com.github.pokatomnik.kriper.services.db.dao.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "history",
    indices = [
        Index(value = ["title"], unique = true)
    ]
)
data class HistoryItem(
    @PrimaryKey @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "lastOpen") val lastOpen: Long,
    @ColumnInfo(name = "scrollPosition") val scrollPosition: Int,
)