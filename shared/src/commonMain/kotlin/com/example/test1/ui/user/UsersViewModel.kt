package com.example.test1.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.core.AppResult
import com.example.test1.core.AppError
import com.example.test1.core.ToastService
import com.example.test1.data.model.NetworkUser
import com.example.test1.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserUiState(
    val users: List<NetworkUser> = emptyList(),
    val isLoading: Boolean = false,
    val error: AppError? = null,
)


class ViewModel2 (
    private val repository: AppRepository,
    private val toastService: ToastService
): ViewModel(){

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }


    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            toastService.info("Loading", "Fetching users from repository...")

            val usersResult = repository.getUsers()

            _uiState.update { state ->
                when (usersResult) {
                    is AppResult.Success -> {
                        toastService.success("Success", "Loaded ${usersResult.data.size} users.")
                        state.copy(isLoading = false, users = usersResult.data, error = null)
                    }
                    is AppResult.Error -> {
                        toastService.fail("Load Failed", "Detailed error: ${usersResult.error}")
                        state.copy(isLoading = false, error = usersResult.error)
                    }
                    is AppResult.Loading -> state.copy(isLoading = true)
                }
            }
        }
    }

    fun clearUsers(){
        _uiState.update {
            it.copy(users = emptyList(), isLoading = false, error = null)
        }
        toastService.info("Cleared", "User list has been emptied.")
    }
}
