package com.example.test1.repository

import com.example.test1.ApiService
import com.example.test1.model.NetworkUser
import com.example.test1.model.NewsItem
import com.example.test1.model.NewsResponse
import com.example.test1.AppResult
import kotlinx.serialization.json.Json

class AppRepository(
    private val apiService: ApiService,
    private val localAssetDataSource: LocalAssetDataSource
) {
    private val json = Json { ignoreUnknownKeys = true }
    private companion object {
        const val NEWS_ASSET_PATH = "files/news.json"
        const val USERS_ASSET_PATH = "files/users.json"
    }

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
                AppResult.Error("Failed to load news from API and Assets", networkError)
            }
        }
    }

    suspend fun getNewsById(id: Int): AppResult<NewsItem> {
        return try {
            val response = apiService.fetchNewsById(id)
            AppResult.Success(response)
        } catch (e: Exception) {
            AppResult.Error("Failed to load news detail: ${e.message}")
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
                AppResult.Error("Failed to load users from API and Assets", networkError)
            }
        }
    }

    fun saveNews(newsItem: NewsItem) {
        println("Saving news item: ${newsItem.title}")
    }
}
