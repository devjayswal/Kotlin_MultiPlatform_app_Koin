package com.example.test1.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.example.test1.ui.components.UsersComponents
import com.example.test1.ui.viewModels.ViewModel2

@Composable
fun View2(viewModel: ViewModel2, paddingValues: PaddingValues) {
    UsersComponents(paddingValues = paddingValues)
}
