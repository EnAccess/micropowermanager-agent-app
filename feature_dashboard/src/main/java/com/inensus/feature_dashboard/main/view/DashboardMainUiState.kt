package com.inensus.feature_dashboard.main.view

sealed class DashboardMainUiState {
    object Loading : DashboardMainUiState()
    object Success : DashboardMainUiState()
    object Error : DashboardMainUiState()
}