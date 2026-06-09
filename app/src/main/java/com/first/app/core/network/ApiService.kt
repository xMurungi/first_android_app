package com.first.app.core.network

import com.first.app.data.models.ApiListResponse
import com.first.app.data.models.LoginApiResponse
import com.first.app.data.models.LoginRequest
import com.first.app.data.models.RegisterRequest
import com.first.app.data.models.ServiceDto
import com.first.app.data.models.SubscribeRequest
import com.first.app.data.models.SubscriptionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import okhttp3.ResponseBody

interface ApiService {

    @POST("api/v1/user/register")
    suspend fun register(@Body request: RegisterRequest): Response<Void>

    @POST("api/v1/access/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginApiResponse>

    @GET("api/v1/service/services")
    suspend fun getServices(): Response<ApiListResponse<ServiceDto>>

    @POST("api/v1/subscription/subscribe")
    suspend fun subscribe(@Body request: SubscribeRequest): Response<Void>

    @GET("api/v1/subscription/subscriptions/{subscriberEmail}")
    suspend fun getSubscriptions(
        @Path("subscriberEmail") email: String
    ): Response<ResponseBody>
}