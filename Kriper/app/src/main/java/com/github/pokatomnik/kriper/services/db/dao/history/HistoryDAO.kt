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

    @Query("SELECT * FROM history WHERE title = :title")
    abstract suspend fun getByTitle(title: String): HistoryItem?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insert(historyItem: HistoryItem): Long

    @Query("UPDATE history SET scrollPosition = :scrollPosition, lastOpen = :lastOpen WHERE title = :title")
    protected abstract suspend fun setScrollPositionForExisting(
        title: String,
        scrollPosition: Int,
        lastOpen: Long
    )

    @Transaction
    open suspend fun setScrollPosition(title: String, scrollPosition: Int) {
        val currentTimeMilliseconds = Calendar.getInstance().time.time
        val historyItem = HistoryItem(
            title = title,
            lastOpen = currentTimeMilliseconds,
            scrollPosition = scrollPosition
        )
        val result = insert(historyItem)

        if (result == -1L) {
            setScrollPositionForExisting(
                title = title,
                scrollPosition = scrollPosition,
                lastOpen = currentTimeMilliseconds
            )
        }
    }
}