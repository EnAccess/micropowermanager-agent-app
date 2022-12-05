package com.inensus.feature_payment.payment_graph.view

import com.github.mikephil.charting.formatter.ValueFormatter

class PaymentGraphXAxisFormatter(private val list: ArrayList<String>) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return list[if (value >= list.size) (list.size - 1) else value.toInt()]
    }
}