package com.inensus.core_network

import com.google.gson.Gson
import com.inensus.core_network.model.Error
import com.inensus.core_network.model.ServiceError
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException

fun Throwable.toServiceError(): ServiceError {
    Timber.e(this)

    return when (this) {
        is HttpException -> parse(response()?.errorBody()?.string())
        is SocketTimeoutException -> ServiceError(Error("Request has been timed out. Please check your connection and try again"))
        else -> ServiceError(Error("An error occurred. Please try again later"))
    }
}

private fun parse(errorBody: String?): ServiceError =
    try {
        Gson().fromJson(errorBody, ServiceError::class.java)
    } catch (e: Throwable) {
        Timber.e(e)
        ServiceError(Error("An error occurred. Please try again later"))
    }