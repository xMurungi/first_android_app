package com.first.app.core

object Constants {
    const val BASE_URL = "https://mawingu.cbaloop.com/cba/"

    // Endpoints — verified against Swagger
    const val REGISTER          = "api/v1/user/register"
    const val LOGIN             = "api/v1/access/login"
    const val GET_SERVICES      = "api/v1/service/services"
    const val SUBSCRIBE         = "api/v1/subscription/subscribe"
    const val GET_SUBSCRIPTIONS = "api/v1/subscription/subscriptions"

    // DataStore
    const val PREFS_NAME = "first_prefs"
    const val KEY_TOKEN  = "auth_token"
    const val KEY_EMAIL  = "user_email"
    const val KEY_NAME   = "user_name"
}