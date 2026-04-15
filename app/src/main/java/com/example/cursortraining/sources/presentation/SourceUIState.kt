package com.example.cursortraining.sources.presentation

sealed interface SourceUIState {
    data object Loading : SourceUIState
    data class Success(val sources: List<Source>) : SourceUIState
    data class Error(val message: String) : SourceUIState
}
