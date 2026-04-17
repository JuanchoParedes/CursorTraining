package com.example.cursortraining.articles.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleData(
    val source: ArticleSourceData,
    val title: String,
    val description: String?,
    @param:Json(name = "urlToImage")
    val imageUrl: String?,
    @param:Json(name = "publishedAt")
    val date: String,
)