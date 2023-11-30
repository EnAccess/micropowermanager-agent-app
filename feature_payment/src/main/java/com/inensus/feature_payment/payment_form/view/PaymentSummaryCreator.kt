package com.inensus.feature_payment.payment_form.view

import android.content.Context
import com.inensus.core.utils.AmountUtils.convertAmountToString
import com.inensus.feature_payment.R
import com.inensus.core_ui.key_value.KeyValue
import java.math.BigDecimal

class PaymentSummaryCreator(private val context: Context) {

    fun createSummaryItems(name: String, surname: String, device: String, amount: BigDecimal) =
        arrayListOf(
            KeyValue.Default(
                context.getString(R.string.payment_name_surname),
                "$name $surname"
            ),
            KeyValue.Default(
                context.getString(R.string.payment_device),
                device
            ),
            KeyValue.Amount(
                context.getString(R.string.payment_amount),
                convertAmountToString(amount),
                context.getString(R.string.default_currency)
            )
        )
}