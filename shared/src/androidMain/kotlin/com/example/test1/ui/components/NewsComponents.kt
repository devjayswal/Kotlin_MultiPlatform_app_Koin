package com.example.test1.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.test1.ui.SharedTestViewModel
import com.example.test1.ui.viewModels.ViewModel3
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewsComponents(){
    val viewModel = koinViewModel<ViewModel3>()
    val uiState by viewModel.uiState.collectAsState()


    Column(modifier = Modifier.fillMaxSize()) {
        // Data Section
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            uiState.error?.let {
                Text("Error: $it", color = MaterialTheme.colorScheme.error)
            }


            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item { Text("News", style = MaterialTheme.typography.titleMedium) }
                items(uiState.news) { item ->
                    Card(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(item.title, fontWeight = FontWeight.Bold)
                            Text(item.summary ?: "", style = MaterialTheme.typography.bodySmall, maxLines = 2)
                        }
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }

            }
        }
    }
}