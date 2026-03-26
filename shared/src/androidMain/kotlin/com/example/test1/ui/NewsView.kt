package com.example.test1.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.example.test1.ui.components.NewsComponents
import com.example.test1.ui.viewModels.ViewModel3

@Composable
fun View3(viewModel: ViewModel3, paddingValues: PaddingValues) {
    NewsComponents(viewModel, paddingValues)
}
