package com.example.test1.data.remote

import com.example.test1.data.model.AuthRequest
import com.example.test1.data.model.AuthResponse
import com.example.test1.data.model.NetworkUser
import com.example.test1.data.model.NewsItem
import com.example.test1.data.model.NewsResponse
import com.example.test1.data.model.RefreshTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

internal class KtorApiService(
    private val httpClient: HttpClient
) : ApiService {

    override suspend fun fetchNews(limit: Int, offset: Int): NewsResponse {
        val response = httpClient.get("articles/") {
            parameter("limit", limit)
            parameter("offset", offset)
        }
        if (!response.status.isSuccess()) {
            throw IllegalStateException("News request failed with HTTP ${response.status.value}")
        }
        return response.body()
    }

    override suspend fun fetchNewsById(id: Int): NewsItem {
        val response = httpClient.get("articles/$id/")
        if (!response.status.isSuccess()) {
            throw IllegalStateException("News detail request failed with HTTP ${response.status.value}")
        }
        return response.body()
    }

    override suspend fun fetchUsers(): List<NetworkUser> {
        val response = httpClient.get("users/")
        if (!response.status.isSuccess()) {
            throw IllegalStateException("Users request failed with HTTP ${response.status.value}")
        }
        return response.body()
    }

    override suspend fun login(username: String, password: String): AuthResponse {
        val response = httpClient.post("auth/login") {
            setBody(AuthRequest(username, password))
        }
        if (!response.status.isSuccess()) {
            throw IllegalStateException("Login failed with HTTP ${response.status.value}")
        }
        return response.body()
    }

    override suspend fun refreshToken(refreshToken: String): AuthResponse {
        val response = httpClient.post("auth/refresh") {
            setBody(RefreshTokenRequest(refreshToken))
        }
        if (!response.status.isSuccess()) {
            throw IllegalStateException("Token refresh failed with HTTP ${response.status.value}")
        }
        return response.body()
    }
}