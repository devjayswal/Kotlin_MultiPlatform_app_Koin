package com.example.test1.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager(private val dataStore: DataStore<Preferences>) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")

    val accessToken: Flow<String?> = dataStore.data.map { it[accessTokenKey] }
    val refreshToken: Flow<String?> = dataStore.data.map { it[refreshTokenKey] }

    suspend fun saveTokens(access: String, refresh: String) {
        dataStore.edit { prefs ->
            prefs[accessTokenKey] = access
            prefs[refreshTokenKey] = refresh
        }
    }

    suspend fun clearTokens() {
        dataStore.edit { prefs ->
            prefs.remove(accessTokenKey)
            prefs.remove(refreshTokenKey)
        }
    }
}
