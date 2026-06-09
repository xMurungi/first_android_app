package com.first.app.data.repository

import com.first.app.core.network.ApiService
import com.first.app.core.storage.TokenDataStore
import com.first.app.data.models.LoginRequest
import com.first.app.data.models.RegisterRequest
import com.first.app.data.models.Result

class AuthRepository(
    private val api: ApiService,
    private val dataStore: TokenDataStore
) {

    suspend fun register(
        fullName: String,
        email: String,
        msisdn: String,
        credentials: String
    ): Result<String> {
        return try {
            val response = api.register(
                RegisterRequest(
                    fullName = fullName,
                    email = email,
                    msisdn = msisdn,
                    credentials = credentials
                )
            )
            // 200 with empty body = success, don't try to parse the body
            if (response.isSuccessful) {
                Result.Success("Registration successful")
            } else {
                Result.Error(parseHttpError(response.code()))
            }
        } catch (e: Exception) {
            Result.Error(parseNetworkError(e))
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(email = email, password = password))
            if (response.isSuccessful) {
//                val token = response.body()?.data?.token  // extract ONLY the JWT
                val token = response.body()?.data?.token
                    ?.trim()
                    ?.removeSurrounding("\"") // remove any surrounding quotes
                if (!token.isNullOrEmpty()) {
                    val fullName = response.body()?.data?.fullName ?: ""
                    dataStore.saveAuth(token = token, email = email, name = fullName)
                    Result.Success(token)
                } else {
                    Result.Error("Login failed. Could not retrieve token.")
                }
            } else {
                Result.Error(parseHttpError(response.code()))
            }
        } catch (e: Exception) {
            Result.Error(parseNetworkError(e))
        }
    }

    suspend fun logout() = dataStore.clearAuth()

    suspend fun isLoggedIn() = dataStore.isLoggedIn()
}