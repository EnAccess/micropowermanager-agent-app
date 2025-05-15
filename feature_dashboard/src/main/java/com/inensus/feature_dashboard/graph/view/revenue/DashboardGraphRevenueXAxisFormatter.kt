package com.inensus.feature_dashboard.graph.view.revenue

import com.github.mikephil.charting.formatter.ValueFormatter

class DashboardGraphRevenueXAxisFormatter(
    private val list: ArrayList<String>,
) : ValueFormatter() {
    override fun getFormattedValue(value: Float) = list[value.toInt()]
}
