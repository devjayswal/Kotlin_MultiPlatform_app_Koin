package com.example.test1.core

enum class ToastType {
    SUCCESS,
    FAIL,
    INFO,
    WARNING
}

interface ToastService {
    /**
     * Shows a toast on the UI and logs the full message to the console/logcat.
     * @param message The short message to show on the UI.
     * @param detail Optional detailed message for logging purposes only.
     */
    fun showToast(message: String, type: ToastType = ToastType.INFO, detail: String? = null)
    
    fun success(message: String, detail: String? = null) = showToast(message, ToastType.SUCCESS, detail)
    fun fail(message: String, detail: String? = null) = showToast(message, ToastType.FAIL, detail)
    fun info(message: String, detail: String? = null) = showToast(message, ToastType.INFO, detail)
    fun warning(message: String, detail: String? = null) = showToast(message, ToastType.WARNING, detail)
}
