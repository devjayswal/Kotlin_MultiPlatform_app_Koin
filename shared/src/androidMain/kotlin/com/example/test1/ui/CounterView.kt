package com.example.test1.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.test1.ui.components.CounterComponent
import com.example.test1.ui.counter.CounterViewModel

@Composable
fun View4(viewModel: CounterViewModel, navController: NavController, paddingValues: PaddingValues) {
    CounterComponent(viewModel, navController, paddingValues)
}
