package com.inensus.feature_payment.payment_detail.model

import com.google.gson.annotations.SerializedName

data class PaymentDetailResponse(@SerializedName("data") val data: PaymentDetail)