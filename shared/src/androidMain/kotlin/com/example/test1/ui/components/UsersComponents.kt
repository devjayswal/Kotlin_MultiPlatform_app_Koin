package com.example.test1.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.example.test1.ui.viewModels.ViewModel2
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun UsersComponents() {
    val viewModel = koinViewModel<ViewModel2>()

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
                item { Text("Users", style = MaterialTheme.typography.titleMedium) }
                items(uiState.users) { user ->
                    Card(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("${user.firstName} ${user.lastName}")
                            Text(user.email, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}