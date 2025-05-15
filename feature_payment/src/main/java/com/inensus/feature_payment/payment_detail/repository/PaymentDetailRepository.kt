package com.inensus.feature_payment.payment_detail.repository

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.feature_payment.payment_form.service.PaymentService

class PaymentDetailRepository(
    private val service: PaymentService,
    private val preferences: SharedPreferenceWrapper,
) {
    fun getPaymentDetail(paymentId: Long) = service.getPaymentDetail(preferences.baseUrl + GET_PAYMENT_DETAIL_ENDPOINT + paymentId)

    companion object {
        private const val GET_PAYMENT_DETAIL_ENDPOINT = "transactions/"
    }
}
