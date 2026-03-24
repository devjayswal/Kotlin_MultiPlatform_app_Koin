package com.example.test1.repository

import org.jetbrains.compose.resources.ExperimentalResourceApi
import test1.shared.generated.resources.Res as GeneratedRes

object ResourceLoader {
    @OptIn(ExperimentalResourceApi::class)
    suspend fun readBytes(path: String): ByteArray {
        return GeneratedRes.readBytes(path)
    }
}

