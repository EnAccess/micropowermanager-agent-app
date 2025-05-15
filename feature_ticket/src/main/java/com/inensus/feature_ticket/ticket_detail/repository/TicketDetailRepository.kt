package com.inensus.feature_ticket.ticket_detail.repository

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.feature_ticket.TicketService

class TicketDetailRepository(
    private val service: TicketService,
    private val preferences: SharedPreferenceWrapper,
) {
    fun getTicketDetail(ticketId: String) = service.getTicketDetail(preferences.baseUrl + GET_TICKET_DETAIL_ENDPOINT + ticketId)

    companion object {
        private const val GET_TICKET_DETAIL_ENDPOINT = "app/agents/ticket/"
    }
}
