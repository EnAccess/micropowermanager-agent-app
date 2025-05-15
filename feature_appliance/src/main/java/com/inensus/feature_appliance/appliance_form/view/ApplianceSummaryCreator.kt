package com.inensus.feature_appliance.appliance_form.view

import android.content.Context
import com.inensus.core.utils.AmountUtils.convertAmountToString
import com.inensus.core.utils.DateUtils
import com.inensus.core_network.model.Appliance
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_appliance.R
import java.math.BigDecimal
import java.util.*

class ApplianceSummaryCreator(
    private val context: Context,
) {
    fun createSummaryItems(
        appliance: Appliance,
        firstPaymentDate: Date,
        downPayment: BigDecimal?,
        tenure: Int,
        amount: BigDecimal,
    ) = arrayListOf(
        KeyValue.Default(
            context.getString(R.string.appliance),
            appliance.type.name,
        ),
        KeyValue.Default(
            context.getString(R.string.appliance_payment_date),
            DateUtils.convertDateToString(firstPaymentDate),
        ),
        KeyValue.Amount(
            context.getString(R.string.appliance_down_payment),
            downPayment?.let {
                convertAmountToString(downPayment)
            } ?: context.getString(R.string.payment_amount_hint),
            context.getString(R.string.default_currency),
        ),
        KeyValue.Default(
            context.getString(R.string.appliance_tenure),
            tenure.toString(),
        ),
        KeyValue.Amount(
            context.getString(R.string.appliance_monthly_payment),
            convertAmountToString(amount),
            context.getString(R.string.default_currency),
        ),
        KeyValue.Amount(
            context.getString(R.string.appliance_total_payment),
            convertAmountToString(appliance.cost),
            context.getString(R.string.default_currency),
        ),
    )
}
