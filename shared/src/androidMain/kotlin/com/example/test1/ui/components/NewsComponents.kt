package com.example.test1.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.example.test1.ui.viewModels.ViewModel3
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsComponents() {
    val viewModel = koinViewModel<ViewModel3>()
    val uiState by viewModel.uiState.collectAsState()
    val TAG = "Lifecycle_News"

    // 1. Log every lifecycle event for testing
    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) { Log.d(TAG, "ON_CREATE") }
    LifecycleEventEffect(Lifecycle.Event.ON_START) { Log.d(TAG, "ON_START") }
    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) { Log.d(TAG, "ON_PAUSE") }
    LifecycleEventEffect(Lifecycle.Event.ON_STOP) { Log.d(TAG, "ON_STOP") }
//    LifecycleEventEffect(Lifecycle.Event.ON_DESTROY) { Log.d(TAG, "ON_DESTROY") }

    // 2. Refresh data on Resume
    LifecycleResumeEffect(viewModel) {
        Log.d(TAG, "ON_RESUME: Refreshing News")
        viewModel.loadData()
        onPauseOrDispose {
            Log.d(TAG, "Leaving Resume state")
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.loadData() },
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.news.isEmpty() && uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item { Text("News", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp)) }
                
                items(uiState.news) { item ->
                    Card(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(item.title, fontWeight = FontWeight.Bold)
                            item.summary?.let { Text(it, style = MaterialTheme.typography.bodySmall, maxLines = 3) }
                        }
                    }
                }
            }
        }
    }
}
