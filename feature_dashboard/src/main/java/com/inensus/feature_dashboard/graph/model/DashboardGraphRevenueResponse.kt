package com.inensus.feature_dashboard.graph.model

import com.google.gson.annotations.SerializedName

data class DashboardGraphRevenueResponse(
    val revenues: List<RevenueData>,
)

data class RevenueData(
    @SerializedName("revenue") val revenue: Float,
)
