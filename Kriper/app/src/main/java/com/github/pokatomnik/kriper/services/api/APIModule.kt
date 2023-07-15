package com.github.pokatomnik.kriper.services.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class APIModule {
    @Singleton
    @Provides
    fun provideAPI(): APIService {
        return APIService()
    }
}