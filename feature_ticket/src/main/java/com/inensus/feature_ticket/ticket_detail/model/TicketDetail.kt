package com.inensus.feature_ticket.ticket_detail.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class TicketDetail(
    @SerializedName("ticket_id") val ticketId: String,
    @SerializedName("ticket") val ticket: Ticket?,
    @SerializedName("category") val category: Category?,
    @SerializedName("owner") val owner: Owner?,
    @SerializedName("status") val status: Int,
    @SerializedName("created_at") val createdAt: Date
)

data class Ticket(@SerializedName("desc") val description: String)

data class Category(@SerializedName("label_name") val name: String)

data class Owner(@SerializedName("name") val name: String, @SerializedName("surname") val surname: String)