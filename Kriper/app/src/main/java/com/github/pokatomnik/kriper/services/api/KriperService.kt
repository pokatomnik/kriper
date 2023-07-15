package com.github.pokatomnik.kriper.services.api

import com.github.pokatomnik.kriper.domain.CopyrightHolderBlock
import retrofit2.http.GET

interface KriperService {
    @GET("blocked.json")
    suspend fun getCopyrightHolderBlocks(): List<CopyrightHolderBlock>
}