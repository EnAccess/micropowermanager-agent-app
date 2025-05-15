package com.inensus.feature_payment.payment_graph.service

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface PaymentGraphService {
    @GET
    fun getPaymentGraph(
        @Url url: String,
    ): Single<JsonObject>
}
