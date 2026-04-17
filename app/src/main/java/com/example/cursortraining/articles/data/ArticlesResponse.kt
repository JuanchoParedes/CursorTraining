package com.example.cursortraining.articles.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticlesResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleData>,
)