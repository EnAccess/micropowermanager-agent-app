package com.inensus.feature_dashboard.graph.view.balance

import com.github.mikephil.charting.formatter.ValueFormatter

class DashboardGraphBalanceXAxisFormatter(private val list: ArrayList<String>) : ValueFormatter() {
    override fun getFormattedValue(value: Float) = list[if (list.size == 1) 0 else value.toInt()].substring(5)
}