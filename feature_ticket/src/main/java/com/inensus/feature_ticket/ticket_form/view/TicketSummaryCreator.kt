package com.inensus.feature_ticket.ticket_form.view

import android.content.Context
import com.inensus.core.utils.DateUtils
import com.inensus.core.utils.DateUtils.DATE_FORMAT_FULL
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_ticket.R
import java.util.*

class TicketSummaryCreator(private val context: Context) {

    fun createSummaryItems(name: String, surname: String, message: String, dueDate: Date?, category: String) =
        arrayListOf(
            KeyValue.Default(
                context.getString(R.string.ticket_summary_name_surname),
                "$name $surname"
            ),
            KeyValue.Default(
                context.getString(R.string.ticket_summary_message),
                message
            ),
            KeyValue.Default(
                context.getString(R.string.ticket_summary_due_date),
                DateUtils.convertDateToString(dueDate ?: Date(), DATE_FORMAT_FULL)
            ),
            KeyValue.Default(
                context.getString(R.string.ticket_summary_category),
                category
            )
        )
}