package com.example.test1.ui.components

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test1.ui.SharedTestViewModel
import com.example.test1.ui.viewModels.ViewModel1
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Timer(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<ViewModel1>()
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Timer Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            if (!uiState.isTimerRunning) {
                Text(
                    "Set Timer",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                ) {
                    WheelPicker(count = 10, label = "DD", initialValue = uiState.days, onScroll = { viewModel.updateDays(it) })
                    Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    WheelPicker(count = 24, label = "HH", initialValue = uiState.hours, onScroll = { viewModel.updateHours(it) })
                    Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    WheelPicker(count = 60, label = "MM", initialValue = uiState.minutes, onScroll = { viewModel.updateMinutes(it) })
                    Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    WheelPicker(count = 60, label = "SS", initialValue = uiState.seconds, onScroll = { viewModel.updateSeconds(it) })
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.startTimer() },
                    modifier = Modifier.width(150.dp).height(48.dp)
                ) {
                    Text("START")
                }
            } else {
                // Running State
                Text(
                    "${uiState.days.toString().padStart(2, '0')}:${uiState.hours.toString().padStart(2, '0')}:${uiState.minutes.toString().padStart(2, '0')}:${uiState.seconds.toString().padStart(2, '0')}",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = { viewModel.stopTimer() },
                    modifier = Modifier.width(150.dp).height(48.dp)
                ) {
                    Text("CANCEL")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WheelPicker(
    count: Int,
    label: String,
    initialValue: Int = 0,
    onScroll: (Int) -> Unit
) {
    val state = rememberLazyListState(initialFirstVisibleItemIndex = initialValue)
    
    val currentIndex by remember { 
        derivedStateOf { state.firstVisibleItemIndex } 
    }
    
    LaunchedEffect(currentIndex) {
        onScroll(currentIndex)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(60.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Box(modifier = Modifier.height(100.dp), contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.fillMaxWidth().height(30.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.extraSmall
            ) {}
            
            LazyColumn(
                state = state,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 35.dp)
            ) {
                items(count) { index ->
                    Box(
                        modifier = Modifier.fillMaxWidth().height(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = index.toString().padStart(2, '0'),
                            fontSize = 18.sp,
                            fontWeight = if (currentIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (currentIndex == index) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
