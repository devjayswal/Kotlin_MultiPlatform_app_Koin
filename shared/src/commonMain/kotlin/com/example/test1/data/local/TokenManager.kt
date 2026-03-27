package com.example.test1.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TokenManager {
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    suspend fun saveTokens(access: String, refresh: String)
    suspend fun clearTokens()
}

class TokenManagerImpl(private val dataStore: DataStore<Preferences>) : TokenManager {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")

    override val accessToken: Flow<String?> = dataStore.data.map { it[accessTokenKey] }
    override val refreshToken: Flow<String?> = dataStore.data.map { it[refreshTokenKey] }

    override suspend fun saveTokens(access: String, refresh: String) {
        dataStore.edit { prefs ->
            prefs[accessTokenKey] = access
            prefs[refreshTokenKey] = refresh
        }
    }

    override suspend fun clearTokens() {
        dataStore.edit { prefs ->
            prefs.remove(accessTokenKey)
            prefs.remove(refreshTokenKey)
        }
    }
}
