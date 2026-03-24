package com.example.test1

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object HttpClientFactory {
    private const val BASE_URL = "https://api.spaceflightnewsapi.net/v4/"

    fun create(): HttpClient = HttpClient(createHttpClientEngine()) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) = println(message)
            }
            level = LogLevel.INFO
        }
        defaultRequest {
            url(BASE_URL)
        }
    }
}

