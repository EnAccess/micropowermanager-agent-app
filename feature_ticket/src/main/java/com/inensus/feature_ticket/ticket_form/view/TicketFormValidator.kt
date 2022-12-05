package com.inensus.feature_ticket.ticket_form.view

import com.inensus.feature_ticket.ticket_detail.model.TicketCategory

class TicketFormValidator {
    fun validateForm(message: String?, category: TicketCategory?): List<TicketFormUiState.ValidationError.Error> =
        mutableListOf<TicketFormUiState.ValidationError.Error>().apply {
            if (message.isNullOrBlank()) {
                add(TicketFormUiState.ValidationError.Error.MessageIsBlank)
            }
            if (category == null) {
                add(TicketFormUiState.ValidationError.Error.CategoryIsBlank)
            }
        }
}