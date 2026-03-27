package com.example.test1.core

import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault

class IosToastService : ToastService {
    override fun showToast(message: String, type: ToastType, detail: String?) {
        val prefix = when (type) {
            ToastType.SUCCESS -> "✅ "
            ToastType.FAIL -> "❌ "
            ToastType.INFO -> "ℹ️ "
            ToastType.WARNING -> "⚠️ "
        }
        
        // Log to console on iOS
        if (detail != null) {
            println("[$type] $message | Detail: $detail")
        } else {
            println("[$type] $message")
        }
        
        val alert = UIAlertController.alertControllerWithTitle(
            title = prefix + message,
            message = null, // Keep it short as requested
            preferredStyle = UIAlertControllerStyleAlert
        )
        
        alert.addAction(
            UIAlertAction.actionWithTitle(
                title = "OK",
                style = UIAlertActionStyleDefault,
                handler = null
            )
        )

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(alert, animated = true, completion = null)
    }
}
