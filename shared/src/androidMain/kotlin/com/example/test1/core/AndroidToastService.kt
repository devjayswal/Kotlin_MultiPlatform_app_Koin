package com.example.test1.core

import android.content.Context
import android.util.Log
import android.widget.Toast

class AndroidToastService(
    private val context: Context
) : ToastService {
    private val TAG = "ToastService"

    override fun showToast(message: String, type: ToastType, detail: String?) {
        val prefix = when (type) {
            ToastType.SUCCESS -> "✅ "
            ToastType.FAIL -> "❌ "
            ToastType.INFO -> "ℹ️ "
            ToastType.WARNING -> "⚠️ "
        }
        
        // Log the detailed message to Logcat
        val logMsg = if (detail != null) "[$type] $message | Detail: $detail" else "[$type] $message"
        when (type) {
            ToastType.FAIL -> Log.e(TAG, logMsg)
            ToastType.WARNING -> Log.w(TAG, logMsg)
            else -> Log.i(TAG, logMsg)
        }

        // Show only the short message on the UI
        Toast.makeText(context, "$prefix$message", Toast.LENGTH_SHORT).show()
    }
}
