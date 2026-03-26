package com.example.test1.ui.common

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class CounterViewModel (): ViewModel(){
    var  aNumber = mutableStateOf(0)

    fun increment(){
        aNumber.value++
    }

    fun decrement(){
        aNumber.value--
    }

}