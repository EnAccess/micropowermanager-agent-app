package com.inensus.feature_payment.payment_form.service

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.feature_payment.payment_form.model.ConfirmPaymentRequest

class PaymentFormRepository(
    private val service: PaymentService,
    private val preferences: SharedPreferenceWrapper,
) {
    var meter: String? = null
    var amount: String? = null

    fun confirmPayment() = service.confirmPayment(preferences.baseUrl + CONFIRM_PAYMENT_ENDPOINT, ConfirmPaymentRequest(meter, amount))

    companion object {
        private const val CONFIRM_PAYMENT_ENDPOINT = "transactions/agent"
    }
}
