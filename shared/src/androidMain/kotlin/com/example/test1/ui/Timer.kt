package com.example.test1.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Timer(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<SharedTestViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!uiState.isRunning) {
            Text(
                "Set Timer",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            ) {
                WheelPicker(count = 10, label = "DD", onScroll = { viewModel.updateDays(it) })
                Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                WheelPicker(count = 24, label = "HH", onScroll = { viewModel.updateHours(it) })
                Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                WheelPicker(count = 60, label = "MM", onScroll = { viewModel.updateMinutes(it) })
                Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                WheelPicker(count = 60, label = "SS", onScroll = { viewModel.updateSeconds(it) })
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { viewModel.startTimer() },
                modifier = Modifier.width(200.dp).height(56.dp)
            ) {
                Text("START", fontSize = 18.sp)
            }
        } else {
            // Running State
            Text(
                uiState.toFormattedString(),
                fontSize = 50.sp,
                fontWeight = FontWeight.Black,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(Modifier.height(32.dp))
            
            OutlinedButton(
                onClick = { viewModel.stopTimer() },
                modifier = Modifier.width(200.dp).height(56.dp)
            ) {
                Text("CANCEL", fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WheelPicker(
    count: Int,
    label: String,
    onScroll: (Int) -> Unit
) {
    val state = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    
    // Notify viewModel when the centered item changes
    val currentIndex by remember { 
        derivedStateOf { state.firstVisibleItemIndex } 
    }
    
    LaunchedEffect(currentIndex) {
        onScroll(currentIndex)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Box(modifier = Modifier.height(150.dp), contentAlignment = Alignment.Center) {
            // Highlight bar
            Surface(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.small
            ) {}
            
            LazyColumn(
                state = state,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 55.dp) // Centers the items
            ) {
                items(count) { index ->
                    Box(
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = index.toString().padStart(2, '0'),
                            fontSize = 22.sp,
                            fontWeight = if (currentIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (currentIndex == index) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
