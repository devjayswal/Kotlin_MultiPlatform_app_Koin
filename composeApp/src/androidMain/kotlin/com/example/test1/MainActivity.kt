package com.example.test1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val TAG = "LifecycleTest"

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Notification permission granted")
        } else {
            Log.d(TAG, "Notification permission denied")
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val websocketUrl = ""

    fun connectToWebSocket() {}

    fun disconnectFromWebSocket() {}

    fun syncWebSocketMsg(){}

    fun initWebsocket(){}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Activity is being created")
        enableEdgeToEdge()
        initWebsocket()
        checkNotificationPermission()
        setContent {
            App()
        }
    }

    override fun onStart() {
        super.onStart()
        connectToWebSocket()
        Log.d(TAG, "onStart: Activity is becoming visible")
    }

    override fun onResume() {
        super.onResume()
        syncWebSocketMsg()
        Log.d(TAG, "onResume: Activity is now in the foreground (user can interact)")
    }

    override fun onPause() {
        super.onPause()
        disconnectFromWebSocket()
        Log.d(TAG, "onPause: Activity is partially obscured or losing focus")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity is no longer visible")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: Activity is restarting from a stopped state")
    }

    override fun onDestroy() {
        disconnectFromWebSocket()
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity is being destroyed")
    }
}
