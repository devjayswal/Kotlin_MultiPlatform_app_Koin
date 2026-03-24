package com.example.test1.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimerUiState(

    val isLoading: Boolean = false,
    val error: String? = null,
    // Timer state preserved
    val days: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val isTimerRunning: Boolean = false
)

class ViewModel1() : ViewModel() {
    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    // Timer logic preserved
    fun updateDays(days: Int) = _uiState.update { it.copy(days = days) }
    fun updateHours(hours: Int) = _uiState.update { it.copy(hours = hours) }
    fun updateMinutes(minutes: Int) = _uiState.update { it.copy(minutes = minutes) }
    fun updateSeconds(seconds: Int) = _uiState.update { it.copy(seconds = seconds) }

    fun startTimer() {
        if (_uiState.value.isTimerRunning) return
        _uiState.update { it.copy(isTimerRunning = true) }

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
            _uiState.update { it.copy(isTimerRunning = false) }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isTimerRunning = false) }
    }
}
