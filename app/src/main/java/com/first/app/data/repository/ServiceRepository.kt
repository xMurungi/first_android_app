package com.first.app.data.repository

import com.first.app.core.network.ApiService
import com.first.app.core.storage.TokenDataStore
import com.first.app.data.models.ApiListResponse
import com.first.app.data.models.Result
import com.first.app.data.models.ServiceDto
import com.first.app.data.models.SubscribeRequest
import com.first.app.data.models.SubscriptionDto
import kotlinx.coroutines.flow.firstOrNull

class ServiceRepository(
    private val api: ApiService,
    private val dataStore: TokenDataStore
) {

    suspend fun getServices(): Result<List<ServiceDto>> {
        return try {
            val response = api.getServices()
            if (response.isSuccessful) {
                Result.Success(response.body()?.data ?: emptyList())
            } else {
                Result.Error(parseHttpError(response.code()))
            }
        } catch (e: Exception) {
            Result.Error(parseNetworkError(e))
        }
    }

    suspend fun subscribe(service: ServiceDto): Result<String> {
        return try {
            val email = dataStore.email.firstOrNull()
            if (email.isNullOrEmpty()) {
                return Result.Error("Session expired. Please log in again.")
            }
            val response = api.subscribe(
                SubscribeRequest(
                    subscriberEmail = email,
                    serviceName     = service.serviceName,
                    amountPaid      = service.displayPrice
                )
            )
            if (response.isSuccessful) {
                Result.Success("Subscribed to ${service.serviceName} successfully!")
            } else {
                // Parse validation errors from API e.g. "must not be blank"
                Result.Error(parseHttpError(response.code()))
            }
        } catch (e: Exception) {
            Result.Error(parseNetworkError(e))
        }
    }

    suspend fun getSubscriptions(): Result<List<SubscriptionDto>> {
        return try {
            val email = dataStore.email.firstOrNull() ?: ""
            val response = api.getSubscriptions(email)

            // Read raw body regardless of status code
            val rawBody = if (response.isSuccessful) {
                response.body()?.string()
            } else {
                response.errorBody()?.string()
            }

            if (!rawBody.isNullOrEmpty()) {
                val gson = com.google.gson.Gson()
                val type = object : com.google.gson.reflect.TypeToken<ApiListResponse<SubscriptionDto>>() {}.type

                val parsed: ApiListResponse<SubscriptionDto> = gson.fromJson(rawBody, type)
                Result.Success(parsed.data ?: emptyList())
            } else {
                Result.Success(emptyList())
            }
        } catch (e: Exception) {
            Result.Error(parseNetworkError(e))
        }
    }

}