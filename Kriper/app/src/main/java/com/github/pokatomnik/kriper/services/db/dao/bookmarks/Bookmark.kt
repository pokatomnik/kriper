package com.github.pokatomnik.kriper.services.db.dao.bookmarks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookmarks",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
class Bookmark(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "storyId") val storyId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "scrollPosition") val scrollPosition: Int,
    @ColumnInfo(name = "createdAt") val createdAt: Long
)