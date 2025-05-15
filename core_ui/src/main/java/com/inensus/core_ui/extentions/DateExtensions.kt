package com.inensus.core_ui.extentions

import android.widget.DatePicker
import java.util.*

fun DatePicker.getDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, dayOfMonth)
    return calendar.time
}

fun Date.compareWithoutTime(date: Date): Int {
    val first =
        Calendar.getInstance().also {
            it.time = this
        }

    val second =
        Calendar.getInstance().also {
            it.time = date
        }

    return when {
        first.get(Calendar.YEAR) != second.get(Calendar.YEAR) -> {
            first.get(Calendar.YEAR) - second.get(Calendar.YEAR)
        }

        first.get(Calendar.MONTH) != second.get(Calendar.MONTH) -> {
            first.get(Calendar.MONTH) - second.get(Calendar.MONTH)
        }

        else -> first.get(Calendar.DAY_OF_MONTH) - second.get(Calendar.DAY_OF_MONTH)
    }
}
