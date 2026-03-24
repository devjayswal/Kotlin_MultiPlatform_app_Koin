package com.example.test1

import com.example.test1.network.KtorApiService

object NetworkModule {
    private val httpClient = HttpClientFactory.create()
    val apiService: ApiService = KtorApiService(httpClient)
}
