package com.example.test1.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.core.AppResult
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
    val error: String? = null,
)


class ViewModel2 (
    private val repository: AppRepository
): ViewModel(){

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }


    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val usersResult = repository.getUsers()

            _uiState.update { state ->
                var newState = state.copy(isLoading = false)

                when (usersResult) {
                    is AppResult.Success -> newState = newState.copy(users = usersResult.data)
                    else -> {}
                }

                newState
            }
        }
    }

    fun clearUsers(){
        _uiState.update {
            it.copy(users = emptyList(), isLoading = true)
        }

    }
}