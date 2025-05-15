package com.inensus.feature_ticket.ticket_list.view

import com.inensus.feature_ticket.ticket_list.model.Ticket

sealed class TicketListUiState {
    data class Loading(
        val type: LoadingTicketListType,
    ) : TicketListUiState()

    data class Success(
        val tickets: List<Ticket>,
        val type: LoadingTicketListType,
    ) : TicketListUiState()

    object NoMoreData : TicketListUiState()

    object Error : TicketListUiState()

    object Empty : TicketListUiState()

    data class TicketTapped(
        val ticketId: String,
    ) : TicketListUiState()
}
