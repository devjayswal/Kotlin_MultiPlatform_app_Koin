package com.example.test1.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.core.AppResult
import com.example.test1.core.AppError
import com.example.test1.core.NotificationService
import com.example.test1.data.model.NetworkUser
import com.example.test1.data.model.NewsItem
import com.example.test1.data.repository.AppRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val news: List<NewsItem> = emptyList(),
    val users: List<NetworkUser> = emptyList(),
    val isLoading: Boolean = false,
    val error: AppError? = null,
    // Timer state preserved
    val days: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val isTimerRunning: Boolean = false
)

class SharedTestViewModel(
    private val repository: AppRepository,
    private val notificationService: NotificationService
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val newsResult = repository.getNews()
            val usersResult = repository.getUsers()

            _uiState.update { state ->
                var newState = state.copy(isLoading = false)
                
                when (newsResult) {
                    is AppResult.Success -> newState = newState.copy(news = newsResult.data)
                    is AppResult.Error -> newState = newState.copy(error = newsResult.error)
                    else -> {}
                }

                when (usersResult) {
                    is AppResult.Success -> newState = newState.copy(users = usersResult.data)
                    is AppResult.Error -> {
                        if (newState.error == null) {
                            newState = newState.copy(error = usersResult.error)
                        }
                    }
                    else -> {}
                }
                
                newState
            }
        }
    }

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
                val timeString = formatTime(totalSeconds)
                notificationService.showTimerNotification("Timer Running", timeString, true)
                
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
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isTimerRunning = false) }
        notificationService.dismissNotification()
    }

    private fun formatTime(totalSeconds: Int): String {
        val d = totalSeconds / (24 * 3600)
        val h = (totalSeconds % (24 * 3600)) / 3600
        val m = (totalSeconds % 3600) / 60
        val s = totalSeconds % 60
        return if (d > 0) "${d}d ${h}h ${m}m ${s}s" else "${h}h ${m}m ${s}s"
    }
}
