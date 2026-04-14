package com.example.cursortraining.articles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cursortraining.articles.data.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArticleUIState>(ArticleUIState.Loading)
    val uiState: StateFlow<ArticleUIState> = _uiState.asStateFlow()

    init {
        logEmittedState(_uiState.value)
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            updateState(ArticleUIState.Loading)
            runCatching { articleRepository.getArticles() }
                .onSuccess { data ->
                    val articles = data.mapIndexed { index, item ->
                        val article = item.toArticle(index)
                        article.copy(date = formatRelativeDate(item.date))
                    }
                    updateState(ArticleUIState.Success(articles))
                }
                .onFailure { throwable ->
                    if (throwable is CancellationException) throw throwable

                    Timber.e(throwable, "Failed to load articles")
                    updateState(
                        ArticleUIState.Error(
                            "Something went wrong, please try again later",
                        ),
                    )
                }
        }
    }

    private fun updateState(state: ArticleUIState) {
        _uiState.value = state
        logEmittedState(state)
    }

    private fun logEmittedState(state: ArticleUIState) {
        when (state) {
            is ArticleUIState.Loading ->
                Timber.d("ArticleUIState emitted: Loading | data: none")
            is ArticleUIState.Success ->
                Timber.d(
                    "ArticleUIState emitted: Success | data: count=${state.articles.size}, articles=${state.articles}",
                )
            is ArticleUIState.Error ->
                Timber.d(
                    "ArticleUIState emitted: Error | data: message=${state.message}",
                )
        }
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
                else -> if (daysAgo > 1) "$daysAgo days ago" else "Today" // future dates
            }
        }.getOrElse { throwable ->
            Timber.w(throwable, "Failed to format relative article date: %s", isoDate)
            isoDate
        }
    }
}
