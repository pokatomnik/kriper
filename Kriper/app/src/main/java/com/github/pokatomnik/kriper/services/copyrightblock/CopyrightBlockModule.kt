package com.github.pokatomnik.kriper.services.copyrightblock

import android.content.Context
import com.github.pokatomnik.kriper.services.api.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CopyrightBlockModule {
    @Singleton
    @Provides
    fun provideCopyrightBlock(
        @ApplicationContext context: Context,
        apiService: APIService
    ): CopyrightBlock {
        return CopyrightBlock(context, apiService.kriperService)
    }
}