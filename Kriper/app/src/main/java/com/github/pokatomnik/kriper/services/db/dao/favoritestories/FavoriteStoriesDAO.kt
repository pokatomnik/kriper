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

    @Query("SELECT * FROM favorite_stories WHERE id = :storyId")
    protected abstract suspend fun getFavoriteStoryByTitle(storyId: String): FavoriteStory?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun addFavoriteStory(favoriteStory: FavoriteStory)

    @Delete
    protected abstract suspend fun deleteFavoriteStory(favoriteStory: FavoriteStory)

    @Query("DELETE FROM favorite_stories")
    abstract suspend fun clearAllFavoriteTitles()

    @Query("SELECT COUNT(*) FROM favorite_stories")
    abstract suspend fun getFavoriteQuantity(): Int

    suspend fun getAllFavoriteIds(): List<String> {
        return getAllFavoriteStories().map { it.id }.reversed()
    }

    suspend fun isFavorite(storyId: String): Boolean {
        return getFavoriteStoryByTitle(storyId) != null
    }

    suspend fun addToFavorites(storyId: String) {
        addFavoriteStory(
            FavoriteStory(id = storyId)
        )
    }

    suspend fun removeFromFavorites(storyId: String) {
        deleteFavoriteStory(
            FavoriteStory(storyId)
        )
    }
}