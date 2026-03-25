package com.example.test1.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.test1.model.User
import com.example.test1.ui.viewModels.ViewModel4
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun CounterComponent(viewModel: ViewModel4 = koinViewModel(),navController: NavController,) {
    val count by viewModel.aNumber

    val user = User(name="Dev Jayswal",age = 21,city = "Ahmedabad",address = "Ahmedabad",phone = "+919589883539",
        email ="Devjayswal404@gmail.com",isStudent = true,grades = listOf(10,20,30,40,50),
        relatives = listOf(User(name="Shiv Jayswal",age = 23)))
    val userJson = Json.encodeToString(user)

    fun sendToProfile() {

        val encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Count: $count",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column{
            Row{
                Button(onClick = { viewModel.decrement() }) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { viewModel.increment() }) {
                    Text("+")
                }
            }
            Button(onClick = {
                // navigation user with userdata
//                sendToProfile()
                user
                navController.navigate("view5/${userJson}",)
            }) {
                Text("Show the User")
            }
        }
    }
}