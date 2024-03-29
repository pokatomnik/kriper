package com.github.pokatomnik.kriper.services.db.dao.favoritestories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_stories",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
data class FavoriteStory(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
)