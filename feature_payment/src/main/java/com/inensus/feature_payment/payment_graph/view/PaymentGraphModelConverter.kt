package com.inensus.feature_payment.payment_graph.view

import com.github.mikephil.charting.data.BarEntry
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.inensus.feature_payment.payment_graph.model.AccessRateData
import com.inensus.feature_payment.payment_graph.model.DeferredPaymentData
import com.inensus.feature_payment.payment_graph.model.EnergyData
import com.inensus.feature_payment.payment_graph.model.PaymentGraphResponse

class PaymentGraphModelConverter {
    var xAxisList: ArrayList<String> = ArrayList()

    fun fromJsonToData(json: JsonObject): PaymentGraphResponse {
        xAxisList.clear()

        val energyList = mutableListOf<EnergyData>()
        val accessRateList = mutableListOf<AccessRateData>()
        val deferredPaymentList = mutableListOf<DeferredPaymentData>()

        json.keySet().forEach {
            xAxisList.add(it)

            energyList.add(Gson().fromJson(json.getAsJsonObject(it), EnergyData::class.java))
            accessRateList.add(Gson().fromJson(json.getAsJsonObject(it), AccessRateData::class.java))
            deferredPaymentList.add(Gson().fromJson(json.getAsJsonObject(it), DeferredPaymentData::class.java))
        }

        return PaymentGraphResponse(energyList, accessRateList, deferredPaymentList)
    }

    fun fromDataToUiModel(response: PaymentGraphResponse) =
        listOf(
            createEnergyEntries(response.energies),
            createAccessRateEntries(response.accessRates),
            createDeferredPaymentEntries(response.deferredPayments)
        )

    private fun createEnergyEntries(data: List<EnergyData>): List<BarEntry> =
        data.mapIndexed { index, energy -> BarEntry(index.toFloat(), energy.value) }

    private fun createAccessRateEntries(data: List<AccessRateData>): List<BarEntry> =
        data.mapIndexed { index, accessRate -> BarEntry(index.toFloat(), accessRate.value) }

    private fun createDeferredPaymentEntries(data: List<DeferredPaymentData>): List<BarEntry> =
        data.mapIndexed { index, deferredPayment -> BarEntry(index.toFloat(), deferredPayment.value) }
}
