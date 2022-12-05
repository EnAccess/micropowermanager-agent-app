package com.inensus.feature_dashboard.graph.viewmodel

import androidx.lifecycle.LiveData
import com.inensus.core_ui.BaseViewModel
import com.inensus.feature_dashboard.graph.view.revenue.DashboardGraphRevenueUiState
import com.inensus.feature_dashboard.main.service.DashboardRepository

class DashboardGraphRevenueViewModel(repository: DashboardRepository) : BaseViewModel() {

    val graphRevenueData: LiveData<DashboardGraphRevenueUiState> = repository.graphRevenueData
}