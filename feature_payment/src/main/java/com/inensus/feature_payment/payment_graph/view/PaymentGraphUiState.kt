package com.inensus.feature_payment.payment_graph.view

import com.github.mikephil.charting.data.BarEntry

sealed class PaymentGraphUiState {
    data class Success(val periodList: List<String>, val xAxisList: ArrayList<String>, val barData: List<List<BarEntry>>) : PaymentGraphUiState()
}