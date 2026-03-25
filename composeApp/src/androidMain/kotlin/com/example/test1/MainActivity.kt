package com.example.test1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {

    private val TAG = "LifecycleTest"

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
        setContent {
            App()
        }
    }

    override fun onStart() {
        connectToWebSocket()
        super.onStart()
        Log.d(TAG, "onStart: Activity is becoming visible")
    }

    override fun onResume() {
        super.onResume()
        syncWebSocketMsg()
        Log.d(TAG, "onResume: Activity is now in the foreground (user can interact)")
    }

    override fun onPause() {
        super.onPause()
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
