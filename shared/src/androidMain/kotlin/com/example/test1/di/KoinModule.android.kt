package com.example.test1.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

// Context provider for Android - initialized in BaseApplication
object AndroidContextProvider {
    var context: Context? = null
}

// had to implement datastore for secure storage for  auth token and refreshtoken
val Context.dataStore by preferencesDataStore(name = "auth_prefs")

suspend fun Context.saveTokens(accessToken: String, refreshToken: String) {
    val accessKey = stringPreferencesKey("access_token")
    val refreshKey = stringPreferencesKey("refresh_token")
    dataStore.edit { prefs ->
        prefs[accessKey] = accessToken
        prefs[refreshKey] = refreshToken
    }
}

suspend fun Context.getTokens(): Pair<String?, String?> {
    val accessKey = stringPreferencesKey("access_token")
    val refreshKey = stringPreferencesKey("refresh_token")
    val prefs = dataStore.data.first()
    return prefs[accessKey] to prefs[refreshKey]
}
