package com.example.test1

import android.app.Application
import com.example.test1.di.initKoin

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}