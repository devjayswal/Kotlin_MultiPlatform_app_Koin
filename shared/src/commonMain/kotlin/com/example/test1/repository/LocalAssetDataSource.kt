package com.example.test1.repository

interface LocalAssetDataSource {
    suspend fun readText(path: String): String
}
