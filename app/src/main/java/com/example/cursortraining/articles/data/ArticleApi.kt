package com.example.cursortraining.articles.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {

    @GET("top-headlines?category=business")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String,
    ): ArticlesResponse
}
