package com.github.pokatomnik.kriper.services.db.dao.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "history",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
data class HistoryItem(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "lastOpen") val lastOpen: Long,
    @ColumnInfo(name = "scrollPosition") val scrollPosition: Int,
)