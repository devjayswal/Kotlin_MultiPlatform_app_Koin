package com.example.test1.di

import com.example.test1.core.ConnectivityObserver
import com.example.test1.core.NotificationService
import com.example.test1.core.SoundService
import com.example.test1.core.ToastService
import com.example.test1.core.IosToastService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.module.Module
import org.koin.dsl.module

class IosConnectivityObserver : ConnectivityObserver {
    override fun observe(): Flow<ConnectivityObserver.Status> = flowOf(ConnectivityObserver.Status.Available)
}

class IosNotificationService : NotificationService {
    override fun showTimerNotification(title: String, content: String, isOngoing: Boolean) { /* iOS Impl */ }
    override fun dismissNotification() { /* iOS Impl */ }
}

class IosSoundService : SoundService {
    override fun playTick() {
        // iOS sound implementation would go here (e.g. using AVFoundation)
    }
}

actual fun platformModule(): Module = module {
    single<ConnectivityObserver> { IosConnectivityObserver() }
    single<NotificationService> { IosNotificationService() }
    single<SoundService> { IosSoundService() }
    single<ToastService> { IosToastService() }
}
