package com.example.test1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.test1.ui.AuthScreen
import com.example.test1.ui.MainScreen
import com.example.test1.ui.auth.AuthViewModel
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinContext {
        MaterialTheme {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                val viewModel: AuthViewModel = koinViewModel()
                val uiState by viewModel.uiState.collectAsState()

                if (uiState.isAuthenticated) {
                    MainScreen()
                } else {
                    AuthScreen(viewModel = viewModel)
                }
            }
        }
    }
}
