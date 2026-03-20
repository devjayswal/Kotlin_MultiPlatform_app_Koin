package com.example.test1.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(viewModelModule())
    }
}
