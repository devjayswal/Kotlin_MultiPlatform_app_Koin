package com.example.test1

import android.app.Application
import com.example.test1.di.AndroidContextProvider
import com.example.test1.di.closeConnection
import com.example.test1.di.initKoin

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize context provider before Koin setup
        AndroidContextProvider.context = this
        // initiate connections 
        initKoin()
    }
    
    override fun onTerminate() {
        super.onTerminate()
        // close connections
        closeConnection()
    }
}
