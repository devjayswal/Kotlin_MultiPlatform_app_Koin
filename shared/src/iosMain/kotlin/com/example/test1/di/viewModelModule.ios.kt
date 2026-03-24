package com.example.test1.di

import com.example.test1.ui.SharedTestViewModel
import com.example.test1.ui.viewModels.ViewModel1
import com.example.test1.ui.viewModels.ViewModel2
import com.example.test1.ui.viewModels.ViewModel3
import com.example.test1.ui.viewModels.ViewModel4
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun viewModelModule(): Module {
    return module {
        factory { SharedTestViewModel(get()) }
        factory { ViewModel1() }
        factory { ViewModel2() }
        factory { ViewModel3() }
        factory { ViewModel4() }
    }
}

class ViewModelProvider : KoinComponent {
    fun provideSharedTestViewModel() = get<SharedTestViewModel>()
    fun provideViewModel1() = get<ViewModel1>()
    fun provideViewModel2() = get<ViewModel2>()
    fun provideViewModel3() = get<ViewModel3>()
    fun provideViewModel4() = get<ViewModel4>()
}
