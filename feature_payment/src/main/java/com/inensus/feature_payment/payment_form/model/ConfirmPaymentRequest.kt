package com.inensus.feature_payment.payment_form.model

import com.google.gson.annotations.SerializedName

data class ConfirmPaymentRequest(
    @SerializedName("device_serial") val device: String?,
    @SerializedName("amount") val amount: String?
)