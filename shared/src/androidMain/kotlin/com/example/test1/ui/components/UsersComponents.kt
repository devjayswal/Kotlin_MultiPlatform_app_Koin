package com.example.test1.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.example.test1.ui.viewModels.ViewModel2
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UsersComponents() {
    val viewModel = koinViewModel<ViewModel2>()
    val uiState by viewModel.uiState.collectAsState()
    val TAG = "Lifecycle_Users"

    // 1. Log lifecycle events
    LifecycleEventEffect(Lifecycle.Event.ON_START) { Log.d(TAG, "ON_START") }
    LifecycleEventEffect(Lifecycle.Event.ON_STOP) { Log.d(TAG, "ON_STOP") }

    // 2. Refresh on Resume
    LifecycleResumeEffect(viewModel) {
        Log.d(TAG, "ON_RESUME: Refreshing Users")
        viewModel.loadData()
        onPauseOrDispose { }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item { Text("Users", style = MaterialTheme.typography.titleLarge) }
                items(uiState.users) { user ->
                    Card(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("${user.firstName} ${user.lastName}")
                            Text(user.email, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
