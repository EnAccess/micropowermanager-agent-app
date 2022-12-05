package com.inensus.feature_payment.payment_graph.service

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.feature_payment.payment_graph.model.PaymentGraphPeriod

class PaymentGraphRepository(private val service: PaymentGraphService, private val preferences: SharedPreferenceWrapper) {

    val graphPeriods = listOf(
        PaymentGraphPeriod("D", "Daily"),
        PaymentGraphPeriod("W", "Weekly"),
        PaymentGraphPeriod("M", "Monthly"),
        PaymentGraphPeriod("Y", "Yearly")
    )

    fun getPaymentGraphData(customerId: Long?, period: String?) =
        service.getPaymentGraph(preferences.baseUrl + GET_DASHBOARD_GRAPH_REVENUE_ENDPOINT + (if (customerId != null) "$customerId/graph/" else "graph/") + period)

    companion object {
        private const val GET_DASHBOARD_GRAPH_REVENUE_ENDPOINT = "app/agents/customers/"
    }
}