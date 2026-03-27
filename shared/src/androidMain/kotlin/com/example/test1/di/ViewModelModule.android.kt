package com.example.test1.di

import com.example.test1.ui.common.SharedTestViewModel
import com.example.test1.ui.auth.AuthViewModel
import com.example.test1.ui.home.ViewModel1
import com.example.test1.ui.user.ViewModel2
import com.example.test1.ui.news.ViewModel3
import com.example.test1.ui.counter.CounterViewModel
import com.example.test1.ui.user.ProfileViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual fun viewModelModule(): Module {
    return module {
        viewModelOf(::SharedTestViewModel)
        viewModelOf(::ViewModel1)
        viewModelOf(::ViewModel2)
        viewModelOf(::ViewModel3)
        viewModelOf(::CounterViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::AuthViewModel)
    }
}
