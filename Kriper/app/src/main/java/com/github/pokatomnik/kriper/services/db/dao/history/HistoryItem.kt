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
    /**
     * Actually this field should be of Boolean type.
     * But because of the internal case, Int is used here.
     * 1 = true
     * 0 = false
     */
    @ColumnInfo(name = "read") val read: Int
)