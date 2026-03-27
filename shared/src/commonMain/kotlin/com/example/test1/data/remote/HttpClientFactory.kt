package com.example.test1.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object HttpClientFactory {

    private const val BASE_URL = "https://api.spaceflightnewsapi.net/v4/"

    fun create(accessTokenProvider: () -> String?): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(Logging) {
                level = LogLevel.BODY
            }
            defaultRequest {
                url(BASE_URL)
                accessTokenProvider()?.let { token ->
                    header("Authorization", "Bearer $token")
                }
            }
        }
    }
}
