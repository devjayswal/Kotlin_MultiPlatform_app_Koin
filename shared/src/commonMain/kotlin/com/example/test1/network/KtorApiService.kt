package com.example.test1.network

import com.example.test1.ApiService
import com.example.test1.model.NetworkUser
import com.example.test1.model.NewsItem
import com.example.test1.model.NewsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
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
}

