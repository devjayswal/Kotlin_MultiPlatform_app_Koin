package com.example.test1.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.core.AppResult
import com.example.test1.core.AppError
import com.example.test1.data.model.NewsItem
import com.example.test1.data.repository.AppRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsUiState(
    val news: List<NewsItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: AppError? = null,
)

class ViewModel3 (
    private val repository: AppRepository
): ViewModel(){

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadData()
    }


    fun loadData() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, error = null) }

        loadJob = viewModelScope.launch {
            val newsResult = repository.getNews()

            when (newsResult) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, news = newsResult.data) }
                }
                is AppResult.Error -> {
                    if (newsResult.error is AppError.Server.Unauthorized) {
                        handleUnauthorized()
                    } else {
                        _uiState.update { it.copy(isLoading = false, error = newsResult.error) }
                    }
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun handleUnauthorized() {
        viewModelScope.launch {
            val refreshResult = repository.refreshToken()
            if (refreshResult is AppResult.Success) {
                loadData()
            } else {
                _uiState.update { it.copy(isLoading = false) }
                // repository.refreshToken() already calls logout() on failure,
                // and AuthViewModel observes that flow to show Login screen.
            }
        }
    }

    fun clearNews() {
        loadJob?.cancel()
        _uiState.update {
            it.copy(news = emptyList(), isLoading = false, error = null)
        }
    }
}
