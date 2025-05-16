package com.inensus.feature_payment.payment_form.view

import com.inensus.core_ui.key_value.KeyValue

sealed class PaymentFormUiState {
    data class OpenSummary(
        val summaryItems: ArrayList<KeyValue>,
    ) : PaymentFormUiState()

    data class ValidationError(
        val errors: List<Error>,
    ) : PaymentFormUiState() {
        sealed class Error {
            object DeviceIsBlank : Error()

            object AmountIsBlank : Error()
        }
    }
}
