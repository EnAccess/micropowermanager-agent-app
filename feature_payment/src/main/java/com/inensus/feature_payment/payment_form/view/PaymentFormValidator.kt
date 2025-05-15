package com.inensus.feature_payment.payment_form.view

import java.math.BigDecimal

class PaymentFormValidator {
    fun validateForm(
        meter: String?,
        amount: String?,
    ): List<PaymentFormUiState.ValidationError.Error> =
        mutableListOf<PaymentFormUiState.ValidationError.Error>().apply {
            if (meter.isNullOrBlank()) {
                add(PaymentFormUiState.ValidationError.Error.MeterIsBlank)
            }
            if (amount.isNullOrBlank() || BigDecimal(amount).compareTo(BigDecimal.ZERO) == 0) {
                add(PaymentFormUiState.ValidationError.Error.AmountIsBlank)
            }
        }
}
