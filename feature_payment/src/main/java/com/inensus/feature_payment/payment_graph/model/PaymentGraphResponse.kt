package com.inensus.feature_payment.payment_graph.model

import com.google.gson.annotations.SerializedName

data class PaymentGraphResponse(
    val energies: List<EnergyData>,
    val accessRates: List<AccessRateData>,
    val deferredPayments: List<DeferredPaymentData>
)

data class EnergyData(@SerializedName("energy") val value: Float)

data class AccessRateData(@SerializedName("access_rate") val value: Float)

data class DeferredPaymentData(@SerializedName("deferred_payment") val value: Float)