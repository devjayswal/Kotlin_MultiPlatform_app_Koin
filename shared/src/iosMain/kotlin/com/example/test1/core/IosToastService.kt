package com.example.test1.core

import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault

class IosToastService : ToastService {
    override fun showToast(message: String, type: ToastType) {
        val prefix = when (type) {
            ToastType.SUCCESS -> "✅ Success"
            ToastType.FAIL -> "❌ Error"
            ToastType.INFO -> "ℹ️ Info"
            ToastType.WARNING -> "⚠️ Warning"
        }
        
        val alert = UIAlertController.alertControllerWithTitle(
            title = prefix,
            message = message,
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
