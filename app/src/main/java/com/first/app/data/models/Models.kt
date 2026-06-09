package com.first.app.data.models

import com.google.gson.annotations.SerializedName

// ── Register ──────────────────────────────────────────────────

data class RegisterRequest(
    @SerializedName("fullName")    val fullName: String,
    @SerializedName("email")       val email: String,
    @SerializedName("msisdn")      val msisdn: String,
    @SerializedName("credentials") val credentials: String
)

// ── Login ─────────────────────────────────────────────────────

data class LoginRequest(
    @SerializedName("email")    val email: String,
    @SerializedName("password") val password: String
)

// Login returns a raw String token — no wrapper object needed

// ── Services ──────────────────────────────────────────────────

data class ServiceDto(
    @SerializedName("id")              val id: Long,
    @SerializedName("serviceName")     val serviceName: String,
    @SerializedName("pricing")         val pricing: Double,
    @SerializedName("discountPercent") val discountPercent: Double
) {
    // Computed — discountPercent is e.g. 20.0 meaning 20% off
    val isDiscounted: Boolean
        get() = discountPercent > 0.0

    val discountedPrice: Double
        get() = if (isDiscounted) pricing * (1 - discountPercent / 100) else pricing

    val displayPrice: Double
        get() = discountedPrice
}

// ── Subscribe ─────────────────────────────────────────────────

data class SubscribeRequest(
    @SerializedName("subscriberEmail") val subscriberEmail: String,
    @SerializedName("serviceName")     val serviceName: String,
    @SerializedName("amountPaid")      val amountPaid: Double
)

// ── Subscriptions ─────────────────────────────────────────────

data class SubscriptionDto(
    @SerializedName("id")              val id: Long,
    @SerializedName("subscriberEmail") val subscriberEmail: String,
    @SerializedName("serviceName")     val serviceName: String,
    @SerializedName("amountPaid")      val amountPaid: Double
)

// ── Validation error from API ──────────────────────────────────

data class ValidationErrorResponse(
    @SerializedName("errors") val errors: List<Violation>
)

data class Violation(
    @SerializedName("fieldName") val fieldName: String,
    @SerializedName("message")   val message: String
)

// ── Result wrapper ─────────────────────────────────────────────

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

// Login response wrapper — token is inside data{}
data class LoginApiResponse(
    @SerializedName("data")    val data: UserData,
    @SerializedName("message") val message: String?
)

data class UserData(
    @SerializedName("id")       val id: Long,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email")    val email: String,
    @SerializedName("msisdn")   val msisdn: String?,
    @SerializedName("token")    val token: String
)

// Generic wrapper for list responses e.g. services, subscriptions
data class ApiListResponse<T>(
    @SerializedName("data")    val data: List<T>?,
    @SerializedName("message") val message: String?
)