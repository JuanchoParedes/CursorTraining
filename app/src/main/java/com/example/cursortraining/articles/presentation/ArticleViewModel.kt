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
                        ArticleModel.mapArticle(item, index)
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
}
