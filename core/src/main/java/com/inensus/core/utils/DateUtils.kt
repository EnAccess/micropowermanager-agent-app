package com.inensus.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val DATE_FORMAT_DD_MM_YYYY = "dd.MM.yyyy"
    const val DATE_FORMAT_FULL = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    fun convertDateToString(
        date: Date,
        format: String = DATE_FORMAT_DD_MM_YYYY,
    ): String = SimpleDateFormat(format, Locale.getDefault()).format(date)

    fun convertStringToDate(
        dateString: String,
        format: String,
    ): Date? = SimpleDateFormat(format, Locale.getDefault()).parse(dateString)

    fun convertMillisToDateString(
        value: Float,
        format: String,
    ): String = SimpleDateFormat(format, Locale.getDefault()).format(Date(value.toLong()))
}
