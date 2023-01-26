package com.github.pokatomnik.kriper.services.db.dao.favoritestories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class FavoriteStoriesDAO {
    @Query("SELECT * FROM favorite_stories")
    protected abstract suspend fun getAllFavoriteStories(): List<FavoriteStory>

    @Query("SELECT * FROM favorite_stories WHERE title = :storyTitle")
    protected abstract suspend fun getFavoriteStoryByTitle(storyTitle: String): FavoriteStory?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun addFavoriteStory(favoriteStory: FavoriteStory)

    @Delete
    protected abstract suspend fun deleteFavoriteStory(favoriteStory: FavoriteStory)

    suspend fun getAllFavoriteTitles(): List<String> {
        return getAllFavoriteStories().map { it.title }
    }

    suspend fun isFavorite(storyTitle: String): Boolean {
        return getFavoriteStoryByTitle(storyTitle) != null
    }

    suspend fun addToFavorites(storyTitle: String) {
        addFavoriteStory(
            FavoriteStory(title = storyTitle)
        )
    }

    suspend fun removeFromFavorites(storyTitle: String) {
        deleteFavoriteStory(
            FavoriteStory(title = storyTitle)
        )
    }
}