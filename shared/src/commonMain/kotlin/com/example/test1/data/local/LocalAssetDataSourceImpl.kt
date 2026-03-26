package com.example.test1.data.local

class ComposeResourceAssetDataSource : LocalAssetDataSource {
	override suspend fun readText(path: String): String {
		return ResourceLoader.readBytes(path).decodeToString()
	}
}

fun createLocalAssetDataSource(): LocalAssetDataSource = ComposeResourceAssetDataSource()

