package com.example.test1.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.test1.ui.components.CounterComponent
import com.example.test1.ui.viewModels.ViewModel4

@Composable
fun View4(viewModel: ViewModel4, navController: NavController, paddingValues: PaddingValues) {
    CounterComponent(viewModel, navController, paddingValues)
}
