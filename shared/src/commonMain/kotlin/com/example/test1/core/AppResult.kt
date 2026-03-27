package com.example.test1.core

sealed class AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>()
    data class Error(val error: AppError) : AppResult<Nothing>()
    object Loading : AppResult<Nothing>()
}
