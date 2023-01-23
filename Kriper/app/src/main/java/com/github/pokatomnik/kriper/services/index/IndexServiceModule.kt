package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.services.contentreader.ContentReaderService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class IndexServiceModule {
    @Singleton
    @Provides
    fun provideIndexService(contentReaderService: ContentReaderService): IndexService {
        return IndexService(contentReaderService)
    }
}