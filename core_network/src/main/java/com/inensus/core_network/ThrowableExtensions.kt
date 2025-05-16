package com.inensus.core_network

import com.google.gson.*
import com.inensus.core_network.model.ServiceError
import retrofit2.HttpException
import timber.log.Timber
import java.lang.reflect.Type
import java.net.SocketTimeoutException

fun Throwable.toServiceError(): ServiceError {
    Timber.e(this)

    return when (this) {
        is HttpException -> {
            val errorBody = response()?.errorBody()?.string()
            parse(errorBody)
        }
        is SocketTimeoutException ->
            ServiceError(
                ServiceError.Data(listOf("Request has been timed out. Please check your connection and try again"), 0),
            )
        else -> ServiceError(ServiceError.Data(listOf("An error occurred. Please try again later"), 0))
    }
}

private fun parse(errorBody: String?): ServiceError {
    return try {
        if (!errorBody.isNullOrBlank()) {
            val gson =
                GsonBuilder()
                    .registerTypeAdapter(ServiceError::class.java, ServiceErrorDeserializer())
                    .create()
            val errorResponse = gson.fromJson(errorBody, ServiceError::class.java)
            return errorResponse
        } else {
            // If error body is blank or null, create a default error
            ServiceError(ServiceError.Data(listOf("An error occurred. Please try again later"), 0))
        }
    } catch (e: Throwable) {
        Timber.e(e)
        // Handle other exceptions if necessary
        ServiceError(ServiceError.Data(listOf("An error occurred. Please try again later"), 0))
    }
}

class ServiceErrorDeserializer : JsonDeserializer<ServiceError> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): ServiceError {
        val jsonObject = json?.asJsonObject
        val data = jsonObject?.getAsJsonObject("data")
        val messageElement = data?.get("message")

        val message =
            when {
                messageElement?.isJsonArray == true -> {
                    // If "message" is an array, handle it accordingly
                    parseMessageArray(messageElement.asJsonArray)
                }
                messageElement?.isJsonPrimitive == true -> {
                    // If "message" is a single string, convert it to a list
                    listOf(messageElement.asString)
                }
                else -> emptyList()
            }

        return ServiceError(ServiceError.Data(message, data?.get("status_code")?.asInt ?: 0))
    }

    private fun parseMessageArray(jsonArray: JsonArray): List<String> {
        val messageList = mutableListOf<String>()

        jsonArray.forEach { element ->
            if (element.isJsonPrimitive) {
                messageList.add(element.asString)
            } else if (element.isJsonObject) {
                // If the element is an object, extract specific fields or convert it to a string
                val messageString = extractMessageFromJsonObject(element.asJsonObject)
                messageList.add(messageString)
            }
        }

        return messageList
    }
}

private fun extractMessageFromJsonObject(jsonObject: JsonObject): String {
    // Customize this logic based on your JSON structure
    val file = jsonObject.getAsJsonPrimitive("file")?.asString ?: ""
    val line = jsonObject.getAsJsonPrimitive("line")?.asInt ?: 0
    // Extract other fields as needed

    return "$file:$line"
}
