package com.inensus.feature_ticket.ticket_detail.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TicketDetail(
    @SerializedName("id") val ticketId: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("category") val category: Category?,
    @SerializedName("owner") val owner: Owner?,
    @SerializedName("status") val status: Int,
    @SerializedName("created_at") val createdAt: Date,
)

data class Category(
    @SerializedName("label_name") val name: String,
)

data class Owner(
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
)
