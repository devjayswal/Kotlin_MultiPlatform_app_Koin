package com.example.test1.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.core.AppError
import com.example.test1.data.repository.AppRepository
import com.example.test1.core.AppResult
import com.example.test1.core.ToastService
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
    val isLoginMode: Boolean = true,
    val isAppOld: Boolean = false
)

class AuthViewModel(
    private val repository: AppRepository,
    private val toastService: ToastService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeLoginStatus()
    }

    private fun observeLoginStatus() {
        viewModelScope.launch {
            repository.isUserLoggedInFlow.collect { isLoggedIn ->
                _uiState.update { it.copy(isAuthenticated = isLoggedIn) }
                if (isLoggedIn) {
                    toastService.success("Logged In", "Full login sync completed successfully.")
                }
            }
        }
    }

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
        viewModelScope.launch {
            repository.logout()
            toastService.info("Logged out")
        }
    }

    fun authenticate() {
        val currentState = _uiState.value
        
        if (currentState.username.isBlank() || currentState.password.isBlank()) {
            val errorMsg = "Missing fields"
            _uiState.update { it.copy(error = errorMsg) }
            toastService.warning(errorMsg, "User tried to authenticate with empty username or password.")
            return
        }
        
        if (!currentState.isLoginMode && currentState.email.isBlank()) {
            val errorMsg = "Missing email"
            _uiState.update { it.copy(error = errorMsg) }
            toastService.warning(errorMsg, "User tried to sign up without an email.")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = repository.login(currentState.username, currentState.password)
            
            when (result) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    toastService.success("Login Success")
                }
                is AppResult.Error -> {
                    when (result.error) {
                        is AppError.Server.NotFound -> {
                            val errorMsg = "User not found"
                            _uiState.update { it.copy(isLoading = false, error = errorMsg) }
                            toastService.fail(errorMsg, "Server returned 404 for user ${currentState.username}")
                        }
                        is AppError.Server.NotAcceptable -> {
                             _uiState.update { it.copy(isLoading = false, isAppOld = true) }
                             toastService.warning("Update App", "Version mismatch: ${result.error}")
                        }
                        else -> {
                            val errorMsg = "Login Failed"
                            _uiState.update { it.copy(isLoading = false, error = errorMsg) }
                            toastService.fail(errorMsg, "Detailed error: ${result.error}")
                        }
                    }
                }
                else -> {
                    val errorMsg = "Unknown Error"
                    _uiState.update { it.copy(isLoading = false, error = errorMsg) }
                    toastService.fail(errorMsg, "Repository returned unexpected result type.")
                }
            }
        }
    }

    fun dismissUpdatePopup() {
        _uiState.update { it.copy(isAppOld = false) }
    }
}
