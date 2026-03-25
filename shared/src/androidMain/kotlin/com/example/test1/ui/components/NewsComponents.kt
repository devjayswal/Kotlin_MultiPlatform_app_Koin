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
fun NewsComponents(viewModel: ViewModel3 = koinViewModel<ViewModel3>()) {
    val uiState by viewModel.uiState.collectAsState()
    val TAG = "Lifecycle_News"
    
    // Local state to mask the first frame with a loading indicator
    // This prevents seeing old data during the initial composition (Frame 0)
    var isFirstFrame by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isFirstFrame = false
    }

    // 2. Refresh data on Resume and Clear on Dispose
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        Log.d(TAG, "ON_RESUME: Calling loadData")
        viewModel.loadData() // Ensure we load data when screen becomes active
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d(TAG, "DisposableEffect: Calling clearNews")
            viewModel.clearNews() // Clear data when composable is removed from UI tree
        }
    }


    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.loadData() },
        modifier = Modifier.fillMaxSize()
    ) {
        // Show loading if:
        // 1. ViewModel says so (uiState.isLoading)
        // 2. It's the very first frame of composition (masking potential old data)
        if (uiState.isLoading || isFirstFrame) {
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
