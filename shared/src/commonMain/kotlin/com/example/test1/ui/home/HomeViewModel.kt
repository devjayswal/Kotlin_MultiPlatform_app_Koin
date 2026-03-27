package com.example.test1.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.core.AppError
import com.example.test1.core.NotificationService
import com.example.test1.core.SoundService
import com.example.test1.core.ToastService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimerUiState(
    val isLoading: Boolean = false,
    val error: AppError? = null,
    // Timer state preserved
    val days: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val isTimerRunning: Boolean = false
)

class ViewModel1(
    private val notificationService: NotificationService,
    private val soundService: SoundService,
    private val toastService: ToastService
) : ViewModel() {
    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    // Timer logic preserved
    fun updateDays(days: Int) = _uiState.update { it.copy(days = days) }
    fun updateHours(hours: Int) = _uiState.update { it.copy(hours = hours) }
    fun updateMinutes(minutes: Int) = _uiState.update { it.copy(minutes = minutes) }
    fun updateSeconds(seconds: Int) = _uiState.update { it.copy(seconds = seconds) }

    fun triggerServerError() {
        val error = AppError.Server.General(500, "Manually triggered server error")
        _uiState.update { it.copy(error = error) }
        toastService.fail("Server Error", "Detailed error: ${error.message}")
    }

    fun triggerUnknownError() {
        val error = AppError.Unknown("Manually triggered unknown error")
        _uiState.update { it.copy(error = error) }
        toastService.warning("Unexpected Error", "Detail: ${error.message}")
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun startTimer() {
        if (_uiState.value.isTimerRunning) return
        
        val totalSecondsValue = _uiState.value.days * 24 * 3600 +
                _uiState.value.hours * 3600 +
                _uiState.value.minutes * 60 +
                _uiState.value.seconds
        
        if (totalSecondsValue <= 0) {
            toastService.warning("Set Time", "User tried to start timer with 0 duration.")
            return
        }
        
        _uiState.update { it.copy(isTimerRunning = true) }
        toastService.info("Timer Started")

        timerJob = viewModelScope.launch {
            var totalSeconds = totalSecondsValue

            while (totalSeconds > 0) {
                val timeString = formatTime(totalSeconds)
                notificationService.showTimerNotification("Timer Running", timeString, true)
                
                // Play the tick sound
                soundService.playTick()

                delay(1000)
                totalSeconds--

                val d = totalSeconds / (24 * 3600)
                val h = (totalSeconds % (24 * 3600)) / 3600
                val m = (totalSeconds % 3600) / 60
                val s = totalSeconds % 60

                _uiState.update { it.copy(days = d, hours = h, minutes = m, seconds = s) }
            }
            _uiState.update { it.copy(isTimerRunning = false) }
            notificationService.showTimerNotification("Timer Ended", "Your timer has finished!", false)
            toastService.success("Timer Done")
        }
    }

    fun stopTimer() {
        if (_uiState.value.isTimerRunning) {
            timerJob?.cancel()
            _uiState.update { it.copy(isTimerRunning = false) }
            notificationService.dismissNotification()
            toastService.info("Timer Stopped")
        }
    }

    private fun formatTime(totalSeconds: Int): String {
        val d = totalSeconds / (24 * 3600)
        val h = (totalSeconds % (24 * 3600)) / 3600
        val m = (totalSeconds % 3600) / 60
        val s = totalSeconds % 60
        return if (d > 0) "${d}d ${h}h ${m}m ${s}s" else "${h}h ${m}m ${s}s"
    }
}
