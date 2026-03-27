package com.example.test1.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.test1.core.AppError

@Composable
fun ErrorScreen(
    error: AppError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (title, message, icon) = when (error) {
        is AppError.Network -> Triple(
            "No Connection",
            error.message,
            Icons.Default.CloudOff
        )
        is AppError.Server -> {
            val (msg, icon) = when (error) {
                is AppError.Server.NotFound -> error.msg to Icons.Default.SearchOff
                is AppError.Server.Unauthorized -> error.msg to Icons.Default.Lock
                is AppError.Server.PaymentRequired -> error.msg to Icons.Default.Payments
                else -> error.message to Icons.Default.Warning
            }
            Triple("Server Error", msg, icon)
        }
        is AppError.Unknown -> Triple(
            "Oops!",
            error.message,
            Icons.Default.ErrorOutline
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        
        error.code?.let { code ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Error Code: $code",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
