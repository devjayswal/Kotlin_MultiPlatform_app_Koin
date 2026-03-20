package com.example.test1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimerUiState(
    val days: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val isRunning: Boolean = false
) {
    fun toFormattedString(): String {
        return "${days.toString().padStart(2, '0')}:${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }
}

class SharedTestViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState

    private var timerJob: Job? = null

    fun updateDays(days: Int) = _uiState.update { it.copy(days = days) }
    fun updateHours(hours: Int) = _uiState.update { it.copy(hours = hours) }
    fun updateMinutes(minutes: Int) = _uiState.update { it.copy(minutes = minutes) }
    fun updateSeconds(seconds: Int) = _uiState.update { it.copy(seconds = seconds) }

    fun startTimer() {
        if (_uiState.value.isRunning) return
        _uiState.update { it.copy(isRunning = true) }
        
        timerJob = viewModelScope.launch {
            var totalSeconds = _uiState.value.days * 24 * 3600 +
                              _uiState.value.hours * 3600 +
                              _uiState.value.minutes * 60 +
                              _uiState.value.seconds

            while (totalSeconds > 0) {
                delay(1000)
                totalSeconds--
                
                val d = totalSeconds / (24 * 3600)
                val h = (totalSeconds % (24 * 3600)) / 3600
                val m = (totalSeconds % 3600) / 60
                val s = totalSeconds % 60
                
                _uiState.update { it.copy(days = d, hours = h, minutes = m, seconds = s) }
            }
            _uiState.update { it.copy(isRunning = false) }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
    }
}
