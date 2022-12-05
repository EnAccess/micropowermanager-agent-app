package com.inensus.shared_messaging.repository

import com.inensus.shared_messaging.model.MessagingRequest
import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface MessagingService {
    @POST
    fun resetFirebaseToken(@Url url: String, @Body request: MessagingRequest): Completable
}