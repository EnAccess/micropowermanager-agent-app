package com.inensus.feature_payment.payment_form.model

import com.google.gson.annotations.SerializedName

data class ConfirmPaymentRequest(
    @SerializedName("meter_serial_number") val meter: String?,
    @SerializedName("amount") val amount: String?
)