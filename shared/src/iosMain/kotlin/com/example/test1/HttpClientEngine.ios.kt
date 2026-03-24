package com.example.test1

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

internal actual fun createHttpClientEngine(): HttpClientEngine = Darwin.create()

