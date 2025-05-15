package com.inensus.feature_dashboard.graph.model

import com.google.gson.annotations.SerializedName

data class DashboardGraphBalanceResponse(
    val balances: List<BalanceData>,
    val dues: List<DebtData>,
)

data class BalanceData(
    @SerializedName("balance") val balance: Float,
)

data class DebtData(
    @SerializedName("due") val due: Float,
)
