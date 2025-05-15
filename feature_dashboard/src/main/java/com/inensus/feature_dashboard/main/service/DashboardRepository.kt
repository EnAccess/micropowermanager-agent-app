package com.inensus.feature_dashboard.main.service

import androidx.lifecycle.MutableLiveData
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.feature_dashboard.graph.view.balance.DashboardGraphBalanceModelConverter
import com.inensus.feature_dashboard.graph.view.balance.DashboardGraphBalanceUiState
import com.inensus.feature_dashboard.graph.view.revenue.DashboardGraphRevenueModelConverter
import com.inensus.feature_dashboard.graph.view.revenue.DashboardGraphRevenueUiState
import com.inensus.feature_dashboard.summary.model.DashboardSummaryData

class DashboardRepository(
    private val service: DashboardService,
    private val balanceConverter: DashboardGraphBalanceModelConverter,
    private val revenueConverter: DashboardGraphRevenueModelConverter,
    private val preferences: SharedPreferenceWrapper,
) {
    var summaryData: MutableLiveData<DashboardSummaryData> = MutableLiveData()
    var graphBalanceData: MutableLiveData<DashboardGraphBalanceUiState> = MutableLiveData()
    var graphRevenueData: MutableLiveData<DashboardGraphRevenueUiState> = MutableLiveData()

    fun getDashboardSummary() =
        service.getDashboardSummary(preferences.baseUrl + GET_DASHBOARD_SUMMARY_ENDPOINT).doOnSuccess {
            summaryData.postValue(it.data)
        }

    fun getDashboardGraphBalance() =
        service
            .getDashboardGraphBalance(preferences.baseUrl + GET_DASHBOARD_GRAPH_BALANCE_ENDPOINT)
            .doOnSuccess {
                graphBalanceData.postValue(
                    DashboardGraphBalanceUiState.Success(
                        balanceConverter.xAxisList,
                        balanceConverter.fromDataToUiModel(balanceConverter.fromJsonToData(it)),
                    ),
                )
            }

    fun getDashboardGraphRevenue() =
        service
            .getDashboardGraphRevenue(preferences.baseUrl + GET_DASHBOARD_GRAPH_REVENUE_ENDPOINT)
            .doOnSuccess {
                graphRevenueData.postValue(
                    DashboardGraphRevenueUiState.Success(
                        revenueConverter.xAxisList,
                        revenueConverter.fromDataToUiModel(revenueConverter.fromJsonToData(it)),
                    ),
                )
            }

    companion object {
        private const val GET_DASHBOARD_SUMMARY_ENDPOINT = "app/agents/dashboard/boxes"
        private const val GET_DASHBOARD_GRAPH_BALANCE_ENDPOINT = "app/agents/dashboard/graph"
        private const val GET_DASHBOARD_GRAPH_REVENUE_ENDPOINT = "app/agents/dashboard/revenue"
    }
}
