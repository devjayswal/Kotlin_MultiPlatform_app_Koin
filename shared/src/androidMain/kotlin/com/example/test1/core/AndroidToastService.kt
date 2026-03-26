package com.example.test1.core

import android.content.Context
import android.widget.Toast

class AndroidToastService(
    private val context: Context
) : ToastService {
    override fun showToast(message: String, type: ToastType) {
        // In a real app, you might want to customize the toast appearance based on the type
        // For now, we'll just show a standard Toast
        val prefix = when (type) {
            ToastType.SUCCESS -> "✅ Success: "
            ToastType.FAIL -> "❌ Error: "
            ToastType.INFO -> "ℹ️ Info: "
            ToastType.WARNING -> "⚠️ Warning: "
        }
        Toast.makeText(context, "$prefix$message", Toast.LENGTH_SHORT).show()
    }
}
