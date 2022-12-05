package com.inensus.feature_ticket.ticket_detail.model

import com.google.gson.annotations.SerializedName

data class TicketCategory(@SerializedName("id") val id: Long, @SerializedName("label_name") val name: String)