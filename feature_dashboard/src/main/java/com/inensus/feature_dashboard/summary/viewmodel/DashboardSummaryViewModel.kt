package com.inensus.feature_dashboard.summary.viewmodel

import androidx.lifecycle.LiveData
import com.inensus.core_ui.BaseViewModel
import com.inensus.feature_dashboard.main.service.DashboardRepository
import com.inensus.feature_dashboard.summary.model.DashboardSummaryData

class DashboardSummaryViewModel(
    repository: DashboardRepository,
) : BaseViewModel() {
    val summaryData: LiveData<DashboardSummaryData> = repository.summaryData
}
