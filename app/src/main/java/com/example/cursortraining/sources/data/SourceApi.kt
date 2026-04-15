package com.example.cursortraining.sources.data

import retrofit2.http.GET
import retrofit2.http.Query

interface SourceApi {

    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String,
    ): SourcesResponse
}
