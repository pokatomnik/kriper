package com.github.pokatomnik.kriper.services.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class KriperDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): KriperDatabase {
        return Room
            .databaseBuilder(context, KriperDatabase::class.java, "kriper.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}