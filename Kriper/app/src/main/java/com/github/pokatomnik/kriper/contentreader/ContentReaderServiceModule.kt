package com.github.pokatomnik.kriper.contentreader

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ContentReaderServiceModule {
    @Singleton
    @Provides
    fun provideContentReader(application: Application): ContentReaderService {
        return ContentReaderService(application)
    }
}