package com.example.test1.di

import com.example.test1.ui.auth.AuthViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

expect fun viewModelModule(): Module

val authViewModelModule = module {
    factoryOf(::AuthViewModel)
}
