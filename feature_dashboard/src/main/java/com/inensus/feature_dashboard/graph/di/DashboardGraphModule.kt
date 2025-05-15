package com.inensus.feature_dashboard.graph.di

import com.inensus.feature_dashboard.graph.view.balance.DashboardGraphBalanceModelConverter
import com.inensus.feature_dashboard.graph.view.revenue.DashboardGraphRevenueModelConverter
import com.inensus.feature_dashboard.graph.viewmodel.DashboardGraphBalanceViewModel
import com.inensus.feature_dashboard.graph.viewmodel.DashboardGraphRevenueViewModel
import com.inensus.feature_dashboard.main.di.DashboardMainModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DashboardGraphModule {
    fun createDashboardGraphModule() =
        module {
            viewModel { DashboardGraphBalanceViewModel(getScope(DashboardMainModule.DASHBOARD_SCOPE).get()) }
            viewModel { DashboardGraphRevenueViewModel(getScope(DashboardMainModule.DASHBOARD_SCOPE).get()) }

            single { DashboardGraphBalanceModelConverter() }
            single { DashboardGraphRevenueModelConverter() }
        }
}
