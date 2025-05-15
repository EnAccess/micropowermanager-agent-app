package com.inensus.feature_ticket.ticket_detail.model

import com.google.gson.annotations.SerializedName

data class TicketDetailResponse(
    @SerializedName("data") val data: TicketDetail,
)
