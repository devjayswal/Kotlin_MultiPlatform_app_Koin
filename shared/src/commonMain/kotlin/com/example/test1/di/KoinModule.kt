package com.example.test1.di

import com.example.test1.networkModule
import com.example.test1.data.repository.AppRepository
import com.example.test1.data.local.LocalAssetDataSource
import com.example.test1.data.local.createLocalAssetDataSource
import com.example.test1.data.local.TokenManager
import com.example.test1.ui.common.NoOpSoundPlayer
import com.example.test1.ui.common.SoundPlayer
import com.example.test1.core.ConnectivityObserver

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools.synchronized
import org.koin.mp.Lockable

expect fun platformModule(): Module

val commonModule = module {
    single<LocalAssetDataSource> { createLocalAssetDataSource() }
    single<SoundPlayer> { NoOpSoundPlayer }
    single { TokenManager(get()) }
    single { AppRepository(get(), get(), get()) }
}

private val koinLock = Any()
private var koinApplication: KoinApplication? = null

fun initKoin(appDeclaration: KoinAppDeclaration = {}): KoinApplication {
    koinApplication?.let { return it }

    return synchronized(koinLock as Lockable) {
        koinApplication ?: startKoin {
            appDeclaration()
            modules(networkModule, commonModule, viewModelModule(), platformModule())
        }.also { started ->
            koinApplication = started
        }
    }
}

fun resetKoin() {
    synchronized(koinLock as Lockable) {
        stopKoin()
        koinApplication = null
    }
}

fun closeConnection() {
    // Implement connection closing logic here
    // For example, if you have a database connection or network connection, close it here
}
