package com.first.app.data.repository

import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun parseHttpError(code: Int): String = when (code) {
    400 -> "Invalid request. Please check your details."
    401 -> "Invalid credentials. Please try again."
    404 -> "Resource not found."
    409 -> "An account with this email already exists."
    500 -> "Server error. Please try again later."
    else -> "Something went wrong (Error $code)."
}

fun parseNetworkError(e: Exception): String = when (e) {
    is UnknownHostException    -> "No internet connection. Please try again."
    is SocketTimeoutException  -> "Request timed out. Check your connection."
    else                       -> e.message ?: "An unexpected error occurred."
}