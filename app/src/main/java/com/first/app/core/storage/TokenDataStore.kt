package com.first.app.core.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.first.app.core.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = Constants.PREFS_NAME)

class TokenDataStore(private val context: Context) {

    companion object {
        val TOKEN = stringPreferencesKey(Constants.KEY_TOKEN)
        val EMAIL = stringPreferencesKey(Constants.KEY_EMAIL)
        val NAME  = stringPreferencesKey(Constants.KEY_NAME)
    }

    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN] }
    val email: Flow<String?> = context.dataStore.data.map { it[EMAIL] }
    val name:  Flow<String?> = context.dataStore.data.map { it[NAME]  }

    suspend fun saveAuth(token: String, email: String, name: String) {
        context.dataStore.edit {
            it[TOKEN] = token
            it[EMAIL] = email
            it[NAME]  = name
        }
    }

    suspend fun isLoggedIn(): Boolean =
        token.firstOrNull()?.isNotEmpty() == true

    suspend fun clearAuth() = context.dataStore.edit { it.clear() }
}