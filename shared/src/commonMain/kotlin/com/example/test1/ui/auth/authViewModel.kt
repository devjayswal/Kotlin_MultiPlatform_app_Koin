package com.example.test1.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.data.remote.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val isLoginMode: Boolean = true
)

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private var accessToken: String? = null
    private var refreshToken: String? = null

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username, error = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun toggleMode() {
        _uiState.update { it.copy(isLoginMode = !it.isLoginMode, error = null) }
    }

    fun logout() {
        _uiState.update { it.copy(isAuthenticated = false, username = "", password = "", email = "") }
    }

    fun initializeTokens(access: String?, refresh: String?) {
        accessToken = access
        refreshToken = refresh
    }

    fun authenticate() {
        val currentState = _uiState.value
        
        // Basic validation
        if (currentState.username.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(error = "Fields cannot be empty") }
            return
        }
        
        if (!currentState.isLoginMode && currentState.email.isBlank()) {
            _uiState.update { it.copy(error = "Email cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // Simulate network delay
            delay(1000)

            if (currentState.isLoginMode) {
                // Mock Login logic
                if (currentState.username == "admin" && currentState.password == "password") {
                    _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Invalid credentials") }
                }
            } else {
                // Mock Signup logic
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            }
        }
    }

    fun authenticateWithApi(apiService: ApiService) {
        val currentState = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val response = if (currentState.isLoginMode) {
                    apiService.login(currentState.username, currentState.password)
                } else {
                    apiService.login(currentState.username, currentState.password) // Mock signup as login
                }
                accessToken = response.accessToken
                refreshToken = response.refreshToken
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
