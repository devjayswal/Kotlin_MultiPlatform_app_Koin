package com.example.test1.di

import com.example.test1.ui.SharedTestViewModel
import com.example.test1.ui.viewModels.ViewModel1
import com.example.test1.ui.viewModels.ViewModel2
import com.example.test1.ui.viewModels.ViewModel3
import com.example.test1.ui.viewModels.ViewModel4
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual fun viewModelModule(): Module {
    return module {
        viewModelOf(::SharedTestViewModel)
        viewModelOf(::ViewModel1)
        viewModelOf(::ViewModel2)
        viewModelOf(::ViewModel3)
        viewModelOf(::ViewModel4)
    }
}