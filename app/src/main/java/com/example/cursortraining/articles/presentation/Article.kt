package com.example.cursortraining.articles.presentation

import com.example.cursortraining.articles.data.ArticleData
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

data class Article(
    val id: String,
    val sourceId: String?,
    val sourceName: String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val date: String,
)

object ArticleModel {
    fun mapArticle(
        articleData: ArticleData,
        index: Int,
    ): Article {
        val stableId =
            articleData.source.id?.takeIf { it.isNotBlank() }
                ?: "${articleData.source.name}_${articleData.date}_$index"
        return Article(
            id = stableId,
            sourceId = articleData.source.id,
            sourceName = articleData.source.name,
            title = articleData.title,
            description = articleData.description,
            imageUrl = articleData.imageUrl?.takeIf { it.isNotBlank() },
            date = formatRelativeDate(articleData.date),
        )
    }

    private fun formatRelativeDate(isoDate: String): String {
        return runCatching {
            val zoneId = ZoneId.systemDefault()
            val articleLocalDate: LocalDate =
                Instant.parse(isoDate).atZone(zoneId).toLocalDate()

            val today: LocalDate = LocalDate.now(zoneId)
            val daysAgo = ChronoUnit.DAYS.between(articleLocalDate, today).toInt()

            when (daysAgo) {
                0 -> "Today"
                1 -> "Yesterday"
                else -> if (daysAgo > 1) "$daysAgo days ago" else "Today"
            }
        }.getOrElse { throwable ->
            Timber.w(throwable, "Failed to format relative article date: %s", isoDate)
            isoDate
        }
    }
}