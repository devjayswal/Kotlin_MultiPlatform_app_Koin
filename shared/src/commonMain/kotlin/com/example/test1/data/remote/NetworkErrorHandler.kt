package com.example.test1.data.remote

import com.example.test1.core.AppError
import io.ktor.client.plugins.*
import io.ktor.utils.io.errors.*

fun Throwable.toAppError(): AppError {
    return when (this) {
        is IOException -> AppError.Network("No internet connection. Please check your network settings.")
        is ResponseException -> {
            AppError.Server(
                code = response.status.value,
                message = "Server error: ${response.status.description}"
            )
        }
        else -> AppError.Unknown(this.message ?: "An unexpected error occurred")
    }
}
