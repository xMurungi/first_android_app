package com.first.app.core.network

import com.first.app.core.storage.TokenDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val dataStore: TokenDataStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { dataStore.token.firstOrNull() }

//        android.util.Log.d("AuthInterceptor", "Token being sent: '$token'")

        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                // Try Basic Auth encoding instead of Bearer
                val credentials = okhttp3.Credentials.basic(token, "")
                addHeader("Authorization", credentials)
            }
        }.build()

        val response = chain.proceed(request)
        if (response.code == 401) {
            runBlocking { dataStore.clearAuth() }
        }
        return response
    }
}
