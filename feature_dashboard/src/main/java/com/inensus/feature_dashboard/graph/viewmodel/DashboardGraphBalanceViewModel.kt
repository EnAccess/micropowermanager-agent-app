package com.inensus.feature_dashboard.graph.viewmodel

import androidx.lifecycle.LiveData
import com.inensus.core_ui.BaseViewModel
import com.inensus.feature_dashboard.graph.view.balance.DashboardGraphBalanceUiState
import com.inensus.feature_dashboard.main.service.DashboardRepository

class DashboardGraphBalanceViewModel(repository: DashboardRepository) : BaseViewModel() {

    val graphBalanceData: LiveData<DashboardGraphBalanceUiState> = repository.graphBalanceData
}