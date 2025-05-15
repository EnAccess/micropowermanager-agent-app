package com.inensus.feature_dashboard.summary.di

import com.inensus.feature_dashboard.main.di.DashboardMainModule
import com.inensus.feature_dashboard.summary.viewmodel.DashboardSummaryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DashboardSummaryModule {
    fun createDashboardSummaryModule() =
        module { viewModel { DashboardSummaryViewModel(getScope(DashboardMainModule.DASHBOARD_SCOPE).get()) } }
}
