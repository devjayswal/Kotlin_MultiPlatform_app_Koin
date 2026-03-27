package com.example.test1.ui.counter

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.test1.core.ToastService


class CounterViewModel (
    private val toastService: ToastService
): ViewModel(){
    var  aNumber = mutableStateOf(0)

    fun increment(){
        aNumber.value++
        if (aNumber.value % 10 == 0) {
            toastService.success("Reached ${aNumber.value}!")
        }
    }

    fun decrement(){
        aNumber.value--
        if (aNumber.value < 0) {
            toastService.warning("Counter is negative!")
        }
    }

}
