package com.example.cursortraining.sources.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cursortraining.sources.data.SourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SourceViewModel @Inject constructor(
    private val sourceRepository: SourceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SourceUIState>(SourceUIState.Loading)
    val uiState: StateFlow<SourceUIState> = _uiState.asStateFlow()

    init {
        logEmittedState(_uiState.value)
        loadSources()
    }

    fun loadSources() {
        viewModelScope.launch {
            updateState(SourceUIState.Loading)
            runCatching { sourceRepository.getSources() }
                .onSuccess { data ->
                    val sources = data.mapIndexed { index, item ->
                        SourceModel.mapSource(item, index)
                    }
                    updateState(SourceUIState.Success(sources))
                }
                .onFailure { throwable ->
                    if (throwable is CancellationException) throw throwable

                    Timber.e(throwable, "Failed to load sources")
                    updateState(
                        SourceUIState.Error(
                            "Something went wrong, please try again later",
                        ),
                    )
                }
        }
    }

    private fun updateState(state: SourceUIState) {
        _uiState.value = state
        logEmittedState(state)
    }

    private fun logEmittedState(state: SourceUIState) {
        when (state) {
            is SourceUIState.Loading ->
                Timber.d("SourceUIState emitted: Loading | data: none")
            is SourceUIState.Success ->
                Timber.d(
                    "SourceUIState emitted: Success | data: count=${state.sources.size}, sources=${state.sources}",
                )
            is SourceUIState.Error ->
                Timber.d(
                    "SourceUIState emitted: Error | data: message=${state.message}",
                )
        }
    }
}
