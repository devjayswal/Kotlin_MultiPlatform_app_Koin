package com.example.test1.ui.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class ViewModel4 (): ViewModel(){
    var  aNumber = mutableStateOf(0)

    fun increment(){
        aNumber.value++
    }

    fun decrement(){
        aNumber.value--
    }

}