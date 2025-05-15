package com.inensus.feature_dashboard.graph.view.revenue

import com.github.mikephil.charting.data.BarEntry

sealed class DashboardGraphRevenueUiState {
    data class Success(
        val xAxisList: ArrayList<String>,
        val barData: List<BarEntry>,
    ) : DashboardGraphRevenueUiState()
}
