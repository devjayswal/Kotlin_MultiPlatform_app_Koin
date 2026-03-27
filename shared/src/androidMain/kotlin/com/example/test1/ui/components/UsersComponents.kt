package com.example.test1.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.example.test1.ui.common.ErrorScreen
import com.example.test1.ui.user.ViewModel2
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UsersComponents(paddingValues: PaddingValues = PaddingValues(0.dp)) {
    val viewModel = koinViewModel<ViewModel2>()
    val uiState by viewModel.uiState.collectAsState()
    val TAG = "Lifecycle_Users"

    LifecycleEventEffect(Lifecycle.Event.ON_START) { Log.d(TAG, "ON_START") }
    LifecycleEventEffect(Lifecycle.Event.ON_STOP) { Log.d(TAG, "ON_STOP") }

    LifecycleResumeEffect(viewModel) {
        if (uiState.users.isEmpty() && uiState.error == null) {
            viewModel.loadData()
        }
        onPauseOrDispose {
            // Not clearing here to preserve state when navigating back/forth
            // viewModel.clearUsers() 
        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        when {
            uiState.isLoading && uiState.users.isEmpty() -> {
                StyledLoadingScreen()
            }
            uiState.error != null && uiState.users.isEmpty() -> {
                ErrorScreen(
                    error = uiState.error!!,
                    onRetry = { viewModel.loadData() }
                )
            }
            uiState.users.isEmpty() -> {
                // Handle empty state if needed, or just show the title
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                   Text("No members found", style = MaterialTheme.typography.bodyLarge)
                   Button(onClick = { viewModel.loadData() }, modifier = Modifier.padding(top = 16.dp)) {
                       Text("Refresh")
                   }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            "Community Members",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(uiState.users) { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            ListItem(
                                headlineContent = { 
                                    Text("${user.firstName} ${user.lastName}", fontWeight = FontWeight.Bold) 
                                },
                                supportingContent = { 
                                    Text(user.email, style = MaterialTheme.typography.bodySmall) 
                                },
                                leadingContent = {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.Person,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                            )
                        }
                    }
                }
            }
        }
    }
}
