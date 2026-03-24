package com.example.test1.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.AppResult
import com.example.test1.model.NewsItem
import com.example.test1.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsUiState(
    val news: List<NewsItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

class ViewModel3 (
    private val repository: AppRepository
): ViewModel(){

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }


    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val newsResult = repository.getNews()


            _uiState.update { state ->
                var newState = state.copy(isLoading = false)

                when (newsResult) {
                    is AppResult.Success -> newState = newState.copy(news = newsResult.data)
                    is AppResult.Error -> newState = newState.copy(error = newsResult.message)
                    else -> {}
                }

                newState
            }
        }
    }
}