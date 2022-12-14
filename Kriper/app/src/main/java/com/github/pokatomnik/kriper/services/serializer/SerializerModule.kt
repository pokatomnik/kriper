package com.github.pokatomnik.kriper.services.serializer

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SerializerModule {
    @Singleton
    @Provides
    fun provideSerializer(): Serializer {
        return URLSerializer()
    }
}