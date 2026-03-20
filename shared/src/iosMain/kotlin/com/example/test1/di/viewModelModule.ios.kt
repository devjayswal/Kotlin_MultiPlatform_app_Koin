package com.example.test1.di

import com.example.test1.ui.SharedTestViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun viewModelModule(): Module {
    return module {factory { SharedTestViewModel() }}
}

class ViewModelProvider : KoinComponent {

    fun provideSharedTestViewModel() = get<SharedTestViewModel>()

}