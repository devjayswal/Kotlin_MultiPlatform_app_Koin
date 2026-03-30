package com.example.test1.data.remote

import com.example.test1.core.AppError
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*

fun Throwable.toAppError(): AppError {
    return when (this) {
        is IOException -> AppError.Network("Network error: ${this.message ?: "No internet connection"}")
        is ResponseException -> {
            val code = response.status.value
            when (code) {
                HttpStatusCode.NotFound.value -> AppError.Server.NotFound()
                HttpStatusCode.VariantAlsoNegotiates.value -> AppError.Server.VariantAlsoNegotiates()
                HttpStatusCode.PaymentRequired.value -> AppError.Server.PaymentRequired()
                HttpStatusCode.Unauthorized.value -> AppError.Server.Unauthorized()
                HttpStatusCode.InternalServerError.value -> AppError.Server.InternalServerError()
                else -> AppError.Server.General(code, "Server error: ${response.status.description}")
            }
        }
        else -> AppError.Unknown(this.message ?: "An unexpected error occurred")
    }
}
