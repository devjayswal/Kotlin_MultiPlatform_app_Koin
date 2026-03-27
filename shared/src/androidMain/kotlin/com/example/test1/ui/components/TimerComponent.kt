package com.example.test1.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test1.ui.common.ErrorScreen
import com.example.test1.ui.home.ViewModel1
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.abs


@Composable
fun Timer(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val viewModel = koinViewModel<ViewModel1>()
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.error != null) {
        ErrorScreen(
            error = uiState.error!!,
            onRetry = { viewModel.clearError() },
            modifier = Modifier.padding(paddingValues)
        )
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Focus Timer",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Timer Display
                Surface(
                    modifier = Modifier.size(280.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp,
                    shadowElevation = 12.dp,
                    border = borderBrush(
                        brush = Brush.sweepGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (!uiState.isTimerRunning) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth().height(120.dp)
                            ) {
                                WheelPicker(count = 10, label = "DD", initialValue = uiState.days, onScroll = { viewModel.updateDays(it) })
                                Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
                                WheelPicker(count = 24, label = "HH", initialValue = uiState.hours, onScroll = { viewModel.updateHours(it) })
                                Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
                                WheelPicker(count = 60, label = "MM", initialValue = uiState.minutes, onScroll = { viewModel.updateMinutes(it) })
                                Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
                                WheelPicker(count = 60, label = "SS", initialValue = uiState.seconds, onScroll = { viewModel.updateSeconds(it) })
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${uiState.days.toString().padStart(2, '0')}:${uiState.hours.toString().padStart(2, '0')}:${uiState.minutes.toString().padStart(2, '0')}:${uiState.seconds.toString().padStart(2, '0')}",
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Remaining",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (!uiState.isTimerRunning) {
                    Button(
                        onClick = { viewModel.startTimer() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 32.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text("START SESSION", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.triggerServerError() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Server Error", fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = { viewModel.triggerUnknownError() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Unknown Error", fontSize = 12.sp)
                        }
                    }
                } else {
                    Button(
                        onClick = { viewModel.stopTimer() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 32.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        shape = RoundedCornerShape(24.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text("CANCEL", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

private fun borderBrush(brush: Brush): androidx.compose.foundation.BorderStroke {
    return androidx.compose.foundation.BorderStroke(4.dp, brush)
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
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) initialValue
            else {
                val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visibleItemsInfo.minByOrNull { abs((it.offset + it.size / 2) - center) }?.index ?: initialValue
            }
        }
    }
    
    LaunchedEffect(currentIndex) {
        onScroll(currentIndex)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(50.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.height(90.dp), contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.fillMaxWidth().height(36.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {}
            
            LazyColumn(
                state = state,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 27.dp)
            ) {
                items(count) { index ->
                    Box(
                        modifier = Modifier.fillMaxWidth().height(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = index.toString().padStart(2, '0'),
                            fontSize = 20.sp,
                            fontWeight = if (currentIndex == index) FontWeight.ExtraBold else FontWeight.Normal,
                            color = if (currentIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}
