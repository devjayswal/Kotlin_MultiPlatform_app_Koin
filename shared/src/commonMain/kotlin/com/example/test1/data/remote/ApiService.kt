package com.example.test1.data.remote

import com.example.test1.data.model.AuthResponse
import com.example.test1.data.model.NetworkUser
import com.example.test1.data.model.NewsItem
import com.example.test1.data.model.NewsResponse

interface ApiService {
    suspend fun fetchNews(limit: Int = 10, offset: Int = 0): NewsResponse
    suspend fun fetchNewsById(id: Int): NewsItem
    suspend fun fetchUsers(): List<NetworkUser>
    suspend fun login(username: String, password: String): AuthResponse
    suspend fun refreshToken(refreshToken: String): AuthResponse
}