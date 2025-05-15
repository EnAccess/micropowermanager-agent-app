package com.inensus.feature_ticket.ticket_detail.model

import com.google.gson.annotations.SerializedName

data class TicketCategoryResponse(
    @SerializedName("data") val data: List<TicketCategory>,
)
