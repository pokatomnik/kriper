package com.github.pokatomnik.kriper.services.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.pokatomnik.kriper.services.db.dao.bookmarks.Bookmark
import com.github.pokatomnik.kriper.services.db.dao.bookmarks.BookmarksDAO
import com.github.pokatomnik.kriper.services.db.dao.favoritestories.FavoriteStoriesDAO
import com.github.pokatomnik.kriper.services.db.dao.favoritestories.FavoriteStory
import com.github.pokatomnik.kriper.services.db.dao.history.HistoryDAO
import com.github.pokatomnik.kriper.services.db.dao.history.HistoryItem

@Database(
    entities = [HistoryItem::class, FavoriteStory::class, Bookmark::class],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(
            from = 2,
            to = 3,
            spec = KriperDatabase.MigrationFrom2To3::class
        ),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5)
    ]
)
abstract class KriperDatabase : RoomDatabase() {
    abstract fun historyDAO(): HistoryDAO

    abstract fun favoriteStoriesDAO(): FavoriteStoriesDAO

    abstract fun bookmarksDAO(): BookmarksDAO

    @RenameColumn(
        tableName = "history",
        fromColumnName = "title",
        toColumnName = "id"
    )
    @RenameColumn(
        tableName = "favorite_stories",
        fromColumnName = "title",
        toColumnName = "id"
    )
    class MigrationFrom2To3 : AutoMigrationSpec {
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            super.onPostMigrate(db)
            db.execSQL("DELETE FROM favorite_stories")
            db.execSQL("DELETE FROM history")
        }
    }
}