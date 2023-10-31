package com.github.pokatomnik.kriper.services.db.dao.bookmarks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Calendar

@Dao
abstract class BookmarksDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun addBookmarkEntity(bookmark: Bookmark)

    open suspend fun addBookmark(
        storyId: String,
        name: String,
        scrollPosition: Int,
    ) {
        val bookmark = Bookmark(
            storyId = storyId,
            name = name,
            scrollPosition = scrollPosition,
            createdAt = Calendar.getInstance().time.time
        )
        addBookmarkEntity(bookmark)
    }

    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    abstract suspend fun getAll(): List<Bookmark>

    @Query("SELECT * FROM bookmarks WHERE storyId = :storyId")
    abstract suspend fun getByStoryId(storyId: String): List<Bookmark>

    @Query("DELETE FROM bookmarks WHERE id = :bookmarkId")
    abstract suspend fun removeBookmarkByBookmarkId(bookmarkId: Int)

    @Query("DELETE FROM bookmarks WHERE storyId = :storyId")
    abstract suspend fun removeAllBookmarksForStory(storyId: String)

    @Query("DELETE FROM bookmarks")
    abstract suspend fun removeAllBookmarks()
}