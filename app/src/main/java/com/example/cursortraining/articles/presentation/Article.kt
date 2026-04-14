package com.example.cursortraining.articles.presentation

import com.example.cursortraining.articles.data.ArticleData

data class Article(
    val id: String,
    val sourceId: String?,
    val sourceName: String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val date: String,
)

internal fun ArticleData.toArticle(index: Int): Article {
    val stableId = source.id?.takeIf { it.isNotBlank() }
        ?: "${source.name}_${date}_$index"
    return Article(
        id = stableId,
        sourceId = source.id,
        sourceName = source.name,
        title = title,
        description = description,
        imageUrl = imageUrl,
        date = date,
    )
}
