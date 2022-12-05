package com.inensus.feature_dashboard.summary.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.*

data class DashboardSummaryResponse(@SerializedName("data") val data: DashboardSummaryData)

data class DashboardSummaryData(
    @SerializedName("balance") val balance: BigDecimal,
    @SerializedName("profit") val profit: BigDecimal,
    @SerializedName("dept") val debt: BigDecimal,
    @SerializedName("average") val average: BigDecimal,
    @SerializedName("since") val since: Date
)