package com.inensus.feature_ticket.ticket_form.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class ConfirmTicketRequest(
    @SerializedName("title") val title: String?,
    @SerializedName("description") val message: String?,
    @SerializedName("due_date") val dueDate: Date?,
    @SerializedName("label") val category: Long?,
    @SerializedName("owner_id") val customerId: Long?,
)
