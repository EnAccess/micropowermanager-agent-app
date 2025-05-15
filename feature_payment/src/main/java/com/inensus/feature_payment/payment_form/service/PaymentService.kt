package com.inensus.feature_payment.payment_form.service

import com.inensus.feature_payment.payment_detail.model.PaymentDetailResponse
import com.inensus.feature_payment.payment_form.model.ConfirmPaymentRequest
import com.inensus.feature_payment.payment_list.model.PaymentResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface PaymentService {
    @GET
    fun getPayments(
        @Url url: String,
    ): Single<PaymentResponse>

    @GET
    fun getPaymentDetail(
        @Url url: String,
    ): Single<PaymentDetailResponse>

    @POST
    fun confirmPayment(
        @Url url: String,
        @Body request: ConfirmPaymentRequest,
    ): Completable
}
