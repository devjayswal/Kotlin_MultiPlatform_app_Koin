package com.example.test1.core

enum class ToastType {
    SUCCESS,
    FAIL,
    INFO,
    WARNING
}

interface ToastService {
    fun showToast(message: String, type: ToastType = ToastType.INFO)
    fun success(message: String) = showToast(message, ToastType.SUCCESS)
    fun fail(message: String) = showToast(message, ToastType.FAIL)
    fun info(message: String) = showToast(message, ToastType.INFO)
    fun warning(message: String) = showToast(message, ToastType.WARNING)
}
