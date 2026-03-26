package com.example.test1.data.local

interface LocalAssetDataSource {
    suspend fun readText(path: String): String
}