package com.example.test1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.test1.ui.common.ErrorScreen
import com.example.test1.ui.news.ViewModel3
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsComponents(
    viewModel: ViewModel3 = koinViewModel<ViewModel3>(),
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var isFirstFrame by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isFirstFrame = false
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (uiState.news.isEmpty() && uiState.error == null) {
            viewModel.loadData()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearNews()
        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        when {
            (uiState.isLoading || isFirstFrame) && uiState.news.isEmpty() -> {
                StyledLoadingScreen()
            }
            uiState.error != null && uiState.news.isEmpty() -> {
                ErrorScreen(
                    error = uiState.error!!,
                    onRetry = { viewModel.loadData() }
                )
            }
            else -> {
                PullToRefreshBox(
                    isRefreshing = uiState.isLoading,
                    onRefresh = { viewModel.loadData() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item { 
                            Text(
                                "Latest Updates", 
                                style = MaterialTheme.typography.headlineMedium, 
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) 
                        }
                        
                        items(uiState.news) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            "NEWS",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        item.title, 
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    item.summary?.let { 
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            it, 
                                            style = MaterialTheme.typography.bodyMedium, 
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        ) 
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    TextButton(
                                        onClick = { /* Read more */ },
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text("Read full story", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
