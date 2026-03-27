package com.example.test1.data.repository

import com.example.test1.data.remote.ApiService
import com.example.test1.data.model.NetworkUser
import com.example.test1.data.model.NewsItem
import com.example.test1.data.model.NewsResponse
import com.example.test1.core.AppResult
import com.example.test1.core.AppError
import com.example.test1.data.local.LocalAssetDataSource
import com.example.test1.data.local.TokenManager
import com.example.test1.data.remote.toAppError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class AppRepository(
    private val apiService: ApiService,
    private val localAssetDataSource: LocalAssetDataSource,
    private val tokenManager: TokenManager
) {
    private val json = Json { ignoreUnknownKeys = true }
    private companion object {
        const val NEWS_ASSET_PATH = "files/news.json"
        const val USERS_ASSET_PATH = "files/users.json"
    }

    val isUserLoggedInFlow: Flow<Boolean> = tokenManager.accessToken.map { it != null }

    suspend fun getNews(limit: Int = 10, offset: Int = 0): AppResult<List<NewsItem>> {
        return try {
            val response = apiService.fetchNews(limit, offset)
            AppResult.Success(response.results)
        } catch (networkError: Throwable) {
            try {
                val assetData = localAssetDataSource.readText(NEWS_ASSET_PATH)
                val cached = json.decodeFromString<NewsResponse>(assetData)
                AppResult.Success(cached.results)
            } catch (_: Throwable) {
                AppResult.Error(networkError.toAppError())
            }
        }
    }

    suspend fun getNewsById(id: Int): AppResult<NewsItem> {
        return try {
            val response = apiService.fetchNewsById(id)
            AppResult.Success(response)
        } catch (e: Throwable) {
            AppResult.Error(e.toAppError())
        }
    }

    suspend fun getUsers(): AppResult<List<NetworkUser>> {
        return try {
            AppResult.Success(apiService.fetchUsers())
        } catch (networkError: Throwable) {
            try {
                val assetData = localAssetDataSource.readText(USERS_ASSET_PATH)
                val cached = json.decodeFromString<List<NetworkUser>>(assetData)
                AppResult.Success(cached)
            } catch (_: Throwable) {
                AppResult.Error(networkError.toAppError())
            }
        }
    }

    fun saveNews(newsItem: NewsItem) {
        println("Saving news item: ${newsItem.title}")
    }

    suspend fun login(username: String, password: String): AppResult<Boolean> {
        return try {
            val response = apiService.login(username, password)
            tokenManager.saveTokens(response.accessToken, response.refreshToken)
            AppResult.Success(true)
        } catch (e: Exception) {
            AppResult.Error(e.toAppError())
        }
    }

    suspend fun logout() {
        tokenManager.clearTokens()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return tokenManager.accessToken.first() != null
    }

    fun signup() {
        println("Signing up")
    }

    suspend fun refreshToken(): AppResult<Boolean> {
        return try {
            val currentRefreshToken = tokenManager.refreshToken.first()
            if (currentRefreshToken != null) {
                val response = apiService.refreshToken(currentRefreshToken)
                tokenManager.saveTokens(response.accessToken, response.refreshToken)
                AppResult.Success(true)
            } else {
                logout()
                AppResult.Error(AppError.Server.Unauthorized())
            }
        } catch (e: Exception) {
            logout()
            AppResult.Error(e.toAppError())
        }
    }
}
