package com.example.test1.di

import com.example.test1.NetworkModule
import com.example.test1.repository.AppRepository
import com.example.test1.repository.LocalAssetDataSource
import com.example.test1.repository.createLocalAssetDataSource
import com.example.test1.ui.NoOpSoundPlayer
import com.example.test1.ui.SoundPlayer

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools.synchronized
import org.koin.mp.Lockable

val commonModule = module {
    single { NetworkModule.apiService }
    single<LocalAssetDataSource> { createLocalAssetDataSource() }
    single<SoundPlayer> { NoOpSoundPlayer }
    single { AppRepository(get(), get()) }
}

private val koinLock = Any()
private var koinApplication: KoinApplication? = null

fun initKoin(): KoinApplication {
    koinApplication?.let { return it }

    return synchronized(koinLock as Lockable) {
        koinApplication ?: startKoin {
            modules(commonModule, viewModelModule())
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
