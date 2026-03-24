package com.example.test1


import com.example.test1.model.NetworkUser
import com.example.test1.model.NewsItem
import com.example.test1.model.NewsResponse

interface ApiService {
    suspend fun fetchNews(limit: Int = 10, offset: Int = 0): NewsResponse
    suspend fun fetchNewsById(id: Int): NewsItem
    suspend fun fetchUsers(): List<NetworkUser>
}
