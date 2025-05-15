package com.inensus.feature_ticket.ticket_form.view

import com.inensus.core_ui.key_value.KeyValue

sealed class TicketFormUiState {
    data class OpenSummary(
        val summaryItems: ArrayList<KeyValue.Default>,
    ) : TicketFormUiState()

    data class ValidationError(
        val errors: List<Error>,
    ) : TicketFormUiState() {
        sealed class Error {
            object MessageIsBlank : Error()

            object CategoryIsBlank : Error()
        }
    }
}
