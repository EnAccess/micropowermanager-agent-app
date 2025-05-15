package com.inensus.core.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat

object AmountUtils {
    fun convertAmountToString(
        amount: BigDecimal,
        decimalFormat: NumberFormat = DECIMAL_FORMAT_DEFAULT,
    ): String =
        decimalFormat
            .format(amount)
            .replace('\u00A0', ' ')
            .trim()

    private fun getNumberFormat(decimalDigitSize: Int): DecimalFormat =
        (NumberFormat.getCurrencyInstance() as DecimalFormat).apply {
            val symbols = decimalFormatSymbols
            symbols.currencySymbol = ""
            decimalFormatSymbols = symbols
            minimumFractionDigits = decimalDigitSize
            maximumFractionDigits = decimalDigitSize
        }

    private const val DECIMAL_DIGIT_SIZE = 2
    private val DECIMAL_FORMAT_DEFAULT: DecimalFormat = getNumberFormat(DECIMAL_DIGIT_SIZE)
}
