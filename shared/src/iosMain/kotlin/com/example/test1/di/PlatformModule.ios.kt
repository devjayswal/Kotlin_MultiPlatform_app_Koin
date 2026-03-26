package com.example.test1.di

import com.example.test1.core.ConnectivityObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.module.Module
import org.koin.dsl.module

class IosConnectivityObserver : ConnectivityObserver {
    override fun observe(): Flow<ConnectivityObserver.Status> = flowOf(ConnectivityObserver.Status.Available)
}

actual fun platformModule(): Module = module {
    single<ConnectivityObserver> { IosConnectivityObserver() }
}
