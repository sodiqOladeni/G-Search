package com.hackwithsodiq.gsearch.extensions

import com.hackwithsodiq.gsearch.model.Constants
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Int.isSuccessful() = this in 200..299
fun Int.notFound() = this in 400..499

fun Throwable.networkException(): String {
    return when (this) {
        is UnknownHostException -> Constants.UnknownHostException
        is SocketTimeoutException -> Constants.SocketTimeOutException
        else -> this.message ?: Constants.UNKNOWN_ERROR
    }
}