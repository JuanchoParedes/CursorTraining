package com.example.cursortraining.articles.data

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ArticleRepository
    @Inject
    constructor(
        private val articleApi: ArticleApi,
        @Named("news_api_key") private val newsApiKey: String,
    ) {
        suspend fun getArticles(): List<ArticleData> {
            val response =
                articleApi.getTopHeadlines(
                    country = DEFAULT_COUNTRY,
                    apiKey = newsApiKey,
                )
            check(response.status == "ok") {
                "Articles request failed with status: ${response.status}"
            }
            return response.articles
        }

        private companion object {
            const val DEFAULT_COUNTRY = "us"
        }
    }
