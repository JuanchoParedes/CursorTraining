package com.example.cursortraining.sources.data

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SourceRepository @Inject constructor(
    private val sourceApi: SourceApi,
    @Named("news_api_key") private val newsApiKey: String,
) {

    suspend fun getSources(): List<SourceData> {
        val response = sourceApi.getSources(
            country = DEFAULT_COUNTRY,
            category = DEFAULT_CATEGORY,
            apiKey = newsApiKey,
        )
        check(response.status == "ok") {
            "Sources request failed with status: ${response.status}"
        }
        return response.sources
    }

    private companion object {
        const val DEFAULT_COUNTRY = "us"
        const val DEFAULT_CATEGORY = "business"
    }
}
