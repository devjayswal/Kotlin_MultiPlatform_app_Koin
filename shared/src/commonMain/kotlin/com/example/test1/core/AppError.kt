package com.example.test1.core

sealed class AppError(open val code: Int? = null) {
    data class Network(val message: String = "No internet connection") : AppError(1001)
    
    sealed class Server(override val code: Int, val message: String) : AppError(code) {
        data class NotFound(val msg: String = "The requested resource was not found") : Server(404, msg)
        data class NotAcceptable(val msg: String = "App is too old") : Server(406, msg)
        data class VariantAlsoNegotiates(val msg: String = "Server configuration error") : Server(506, msg)
        data class PaymentRequired(val msg: String = "Payment is required to access this resource") : Server(402, msg)
        data class Unauthorized(val msg: String = "You are not authorized to access this resource") : Server(401, msg)
        data class InternalServerError(val msg: String = "Something went wrong on the server") : Server(500, msg)
        data class General(override val code: Int, val msg: String = "Server error occurred") : Server(code, msg)
    }

    data class Unknown(val message: String = "An unexpected error occurred") : AppError(1000)
}
