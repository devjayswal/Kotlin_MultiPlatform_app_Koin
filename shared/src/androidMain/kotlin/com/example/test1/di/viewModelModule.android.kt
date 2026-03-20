package com.example.test1.di

import com.example.test1.ui.SharedTestViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

actual fun viewModelModule(): Module {
    return module {
        viewModel { SharedTestViewModel() }
    }
}