package com.example.test1.data.local

import org.jetbrains.compose.resources.ExperimentalResourceApi
import test1.shared.generated.resources.Res

object ResourceLoader {
    @OptIn(ExperimentalResourceApi::class)
    suspend fun readBytes(path: String): ByteArray {
        return Res.readBytes(path)
    }
}