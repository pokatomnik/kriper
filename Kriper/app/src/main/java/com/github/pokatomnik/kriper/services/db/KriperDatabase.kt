package com.github.pokatomnik.kriper.services.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.pokatomnik.kriper.services.db.dao.favoritestories.FavoriteStoriesDAO
import com.github.pokatomnik.kriper.services.db.dao.favoritestories.FavoriteStory
import com.github.pokatomnik.kriper.services.db.dao.history.HistoryDAO
import com.github.pokatomnik.kriper.services.db.dao.history.HistoryItem

@Database(
    entities = [HistoryItem::class, FavoriteStory::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
//        AutoMigration(
//            from = 2,
//            to = 3,
//            spec = com.github.pokatomnik.kriper.services.db.KriperDatabase.SomeMigration::class
//        )
    ]
)
abstract class KriperDatabase : RoomDatabase() {
    abstract fun historyDAO(): HistoryDAO

    abstract fun favoriteStoriesDAO(): FavoriteStoriesDAO
//    Uncomment when needed
//    @DeleteColumn(tableName = "favorite_stories", columnName = "title")
//    @DeleteColumn(tableName = "recent", columnName = "url")
//    class SomeMigration : AutoMigrationSpec {
//        @Override
//        override fun onPostMigrate(db: SupportSQLiteDatabase) {
//            super.onPostMigrate(db)
//            db.execSQL("DELETE FROM favorite_stories")
//        }
//    }
}