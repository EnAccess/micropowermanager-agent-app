package com.inensus.feature_ticket.ticket_list.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Ticket(
    @SerializedName("ticket_id") val ticketId: String,
    @SerializedName("status") val status: Int,
    @SerializedName("category") val category: Category?,
    @SerializedName("owner") val owner: Owner?,
    @SerializedName("created_at") val createdAt: Date
)

data class Category(@SerializedName("label_name") val name: String)
data class Owner(@SerializedName("name") val name: String, @SerializedName("surname") val surname: String)