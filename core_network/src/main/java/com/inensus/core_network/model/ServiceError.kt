package com.inensus.core_network.model

import com.google.gson.annotations.SerializedName

data class ServiceError(
    @SerializedName("data") val data: Data,
) {
    data class Data(
        @SerializedName("message") val message: List<String>,
        @SerializedName("status_code") val statusCode: Int,
        // Add other fields as needed
    )
}

data class Error(
    val messages: List<String>,
)
