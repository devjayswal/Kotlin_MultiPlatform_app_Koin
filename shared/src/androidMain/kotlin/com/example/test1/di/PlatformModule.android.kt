package com.example.test1.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.test1.core.AndroidConnectivityObserver
import com.example.test1.core.AndroidNotificationService
import com.example.test1.core.AndroidSoundService
import com.example.test1.core.AndroidToastService
import com.example.test1.core.ConnectivityObserver
import com.example.test1.core.NotificationService
import com.example.test1.core.SoundService
import com.example.test1.core.ToastService
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<ConnectivityObserver> { AndroidConnectivityObserver(androidContext()) }
    single<NotificationService> { AndroidNotificationService(androidContext()) }
    single<SoundService> { AndroidSoundService() }
    single<ToastService> { AndroidToastService(androidContext()) }
    single<DataStore<Preferences>> { androidContext().dataStore }
}
