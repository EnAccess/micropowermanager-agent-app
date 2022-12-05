package com.inensus.feature_dashboard.graph.view.balance

import com.github.mikephil.charting.data.Entry
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.inensus.feature_dashboard.graph.model.BalanceData
import com.inensus.feature_dashboard.graph.model.DashboardGraphBalanceResponse
import com.inensus.feature_dashboard.graph.model.DebtData


class DashboardGraphBalanceModelConverter {
    var xAxisList: ArrayList<String> = ArrayList()

    fun fromJsonToData(json: JsonObject): DashboardGraphBalanceResponse {
        val balanceList = mutableListOf<BalanceData>()
        val dueList = mutableListOf<DebtData>()

        json.keySet().forEach {
            xAxisList.add(it)

            balanceList.add(Gson().fromJson(json.getAsJsonObject(it), BalanceData::class.java))
            dueList.add(Gson().fromJson(json.getAsJsonObject(it), DebtData::class.java))
        }

        return DashboardGraphBalanceResponse(balanceList, dueList)
    }

    fun fromDataToUiModel(response: DashboardGraphBalanceResponse) = listOf(
        createGraphBalanceEntries(response.balances),
        createGraphDueEntries(response.dues)
    )

    private fun createGraphBalanceEntries(data: List<BalanceData>): List<Entry> =
        data.mapIndexed { index, balanceData -> Entry(index.toFloat(), balanceData.balance) }

    private fun createGraphDueEntries(data: List<DebtData>): List<Entry> =
        data.mapIndexed { index, debtData -> Entry(index.toFloat(), debtData.due) }
}