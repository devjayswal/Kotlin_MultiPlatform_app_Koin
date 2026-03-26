package com.example.test1.di

import com.example.test1.core.AndroidConnectivityObserver
import com.example.test1.core.ConnectivityObserver
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<ConnectivityObserver> { AndroidConnectivityObserver(androidContext()) }
}
