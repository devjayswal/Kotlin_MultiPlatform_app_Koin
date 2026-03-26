package com.example.test1.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.example.test1.ui.components.Timer
import com.example.test1.ui.viewModels.ViewModel1
@Composable
fun View1(viewModel: ViewModel1, name: Int, paddingValues: PaddingValues) {
    Timer(paddingValues = paddingValues)
}
