package com.example.test1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedTestViewModel: ViewModel() {
    private val _uiState = MutableStateFlow("")

    val uiState: StateFlow<String> = _uiState

    init{
        timer()
    }

    fun timer()= viewModelScope.launch {
        repeat(20){
            delay(1000)
            _uiState.value = it.toString()
        }
    }
}
