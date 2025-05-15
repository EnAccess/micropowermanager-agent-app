package com.inensus.feature_dashboard.summary.view

import com.inensus.feature_dashboard.summary.model.DashboardSummaryData

sealed class DashboardSummaryUiState {
    data class Success(
        val summaryData: DashboardSummaryData,
    ) : DashboardSummaryUiState()
}
