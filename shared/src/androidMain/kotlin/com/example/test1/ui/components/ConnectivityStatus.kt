package com.example.test1.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test1.core.ConnectivityObserver
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun ConnectivityStatus(
    modifier: Modifier = Modifier
) {
    val connectivityObserver = koinInject<ConnectivityObserver>()
    val status by connectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Available)
    
    var showMessage by remember { mutableStateOf(false) }
    var lastStatus by remember { mutableStateOf(ConnectivityObserver.Status.Available) }

    LaunchedEffect(status) {
        if (status == ConnectivityObserver.Status.Available && (lastStatus == ConnectivityObserver.Status.Unavailable || lastStatus == ConnectivityObserver.Status.Lost)) {
            showMessage = true
            delay(3000)
            showMessage = false
        } else if (status != ConnectivityObserver.Status.Available) {
            showMessage = true
        }
        lastStatus = status
    }

    AnimatedVisibility(
        visible = showMessage,
        enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(),
        exit = shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut(),
        modifier = modifier
    ) {
        val (backgroundColor, text) = when (status) {
            ConnectivityObserver.Status.Available -> Color(0xFF4CAF50) to "Back online"
            else -> Color.Black.copy(alpha = 0.8f) to "No connection"
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
