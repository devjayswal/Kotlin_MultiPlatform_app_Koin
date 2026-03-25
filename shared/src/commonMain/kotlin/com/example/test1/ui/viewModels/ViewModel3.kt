package com.example.test1.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.AppResult
import com.example.test1.model.NewsItem
import com.example.test1.repository.AppRepository
import kotlinx.coroutines.Job
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

    private var loadJob: Job? = null

    init {
        loadData()
    }


    fun loadData() {
        println("ViewModel3: loadData called")
        loadJob?.cancel()
        // Set loading state immediately before launching coroutine
        _uiState.update { it.copy(isLoading = true, error = null) }
        println("ViewModel3: Set isLoading=true synchronously")

        loadJob = viewModelScope.launch {
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

    fun clearNews() {
        println("ViewModel3: clearNews called")
        loadJob?.cancel()
        _uiState.update {
            it.copy(news = emptyList(), isLoading = true)
        }
        println("ViewModel3: State cleared (isLoading=true, news=empty)")
    }
}