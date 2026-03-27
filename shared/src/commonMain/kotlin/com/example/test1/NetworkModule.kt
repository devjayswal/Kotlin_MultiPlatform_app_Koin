package com.example.test1

import com.example.test1.data.local.TokenManager
import com.example.test1.data.remote.ApiService
import com.example.test1.data.remote.HttpClientFactory
import com.example.test1.data.remote.KtorApiService
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        val tokenManager = get<TokenManager>()
        HttpClientFactory.create {
            runBlocking {
                tokenManager.accessToken.first()
            }
        }
    }
    
    single<ApiService> {
        KtorApiService(get())
    }
}
