package com.example.cursortraining.articles.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleSourceData(
    val id: String?,
    val name: String,
)