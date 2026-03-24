package com.example.test1.repository

class ComposeResourceAssetDataSource : LocalAssetDataSource {
	override suspend fun readText(path: String): String {
		return ResourceLoader.readBytes(path).decodeToString()
	}
}

fun createLocalAssetDataSource(): LocalAssetDataSource = ComposeResourceAssetDataSource()

