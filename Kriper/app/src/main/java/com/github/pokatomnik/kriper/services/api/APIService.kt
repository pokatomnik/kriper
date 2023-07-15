package com.github.pokatomnik.kriper.services.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIService {
    private val client = OkHttpClient
        .Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://kriper.net/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val kriperService = retrofit.create(KriperService::class.java)
}