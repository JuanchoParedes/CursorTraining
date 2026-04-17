package com.example.cursortraining.articles.presentation

sealed interface ArticleUIState {
    data object Loading : ArticleUIState

    data class Success(val articles: List<Article>) : ArticleUIState

    data class Error(val message: String) : ArticleUIState
}
