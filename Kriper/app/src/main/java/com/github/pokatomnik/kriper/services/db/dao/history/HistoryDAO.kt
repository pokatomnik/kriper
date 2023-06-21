package com.github.pokatomnik.kriper.services.db.dao.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import java.util.Calendar

@Dao
abstract class HistoryDAO {
    @Query("SELECT * FROM history ORDER BY lastOpen DESC")
    abstract suspend fun getAll(): List<HistoryItem>

    @Query("SELECT * FROM history WHERE id = :storyId")
    abstract suspend fun getById(storyId: String): HistoryItem?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insert(historyItem: HistoryItem): Long

    @Query("UPDATE history SET scrollPosition = :scrollPosition, lastOpen = :lastOpen WHERE id = :storyId")
    protected abstract suspend fun setScrollPositionForExisting(
        storyId: String,
        scrollPosition: Int,
        lastOpen: Long
    )

    @Transaction
    open suspend fun setScrollPosition(storyId: String, scrollPosition: Int) {
        val currentTimeMilliseconds = Calendar.getInstance().time.time
        val historyItem = HistoryItem(
            id = storyId,
            lastOpen = currentTimeMilliseconds,
            scrollPosition = scrollPosition
        )
        val result = insert(historyItem)

        if (result == -1L) {
            setScrollPositionForExisting(
                storyId = storyId,
                scrollPosition = scrollPosition,
                lastOpen = currentTimeMilliseconds
            )
        }
    }
}