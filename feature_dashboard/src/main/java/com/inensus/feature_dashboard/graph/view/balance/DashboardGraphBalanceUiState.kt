package com.inensus.feature_dashboard.graph.view.balance

import com.github.mikephil.charting.data.Entry

sealed class DashboardGraphBalanceUiState {
    data class Success(
        val xAxisList: ArrayList<String>,
        val chartData: List<List<Entry>>,
    ) : DashboardGraphBalanceUiState()
}
