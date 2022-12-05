package com.inensus.feature_dashboard.graph.view.revenue

import com.github.mikephil.charting.data.BarEntry
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.inensus.feature_dashboard.graph.model.DashboardGraphRevenueResponse
import com.inensus.feature_dashboard.graph.model.RevenueData

class DashboardGraphRevenueModelConverter {
    var xAxisList: ArrayList<String> = ArrayList()

    fun fromJsonToData(json: JsonObject): DashboardGraphRevenueResponse {
        val data = mutableListOf<RevenueData>()

        json.keySet().forEach {
            xAxisList.add(it)

            data.add(Gson().fromJson(json.getAsJsonObject(it), RevenueData::class.java))
        }

        return DashboardGraphRevenueResponse(data)
    }

    fun fromDataToUiModel(response: DashboardGraphRevenueResponse) = createGraphEntries(response.revenues)

    private fun createGraphEntries(data: List<RevenueData>): List<BarEntry> =
        data.mapIndexed { index, revenueData -> BarEntry(index.toFloat(), revenueData.revenue) }
}