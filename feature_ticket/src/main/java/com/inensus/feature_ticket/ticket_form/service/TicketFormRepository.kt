package com.inensus.feature_ticket.ticket_form.service

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.feature_ticket.TicketService
import com.inensus.feature_ticket.ticket_form.model.ConfirmTicketRequest
import com.inensus.shared_customer.repository.CustomerRepository
import java.util.*

class TicketFormRepository(
    private val service: TicketService,
    private val customerRepository: CustomerRepository,
    private val preferences: SharedPreferenceWrapper,
) {
    var message: String? = null
    var dueDate: Date? = null
    var category: Long? = null

    fun getCategories() =
        service.getCategories(
            preferences.baseUrl?.substring(0, preferences.baseUrl?.length?.minus(4) ?: 0) + GET_TICKET_CATEGORIES_ENDPOINT,
        )

    fun confirmTicket() =
        service.confirmTicket(
            preferences.baseUrl + CONFIRM_TICKET_ENDPOINT,
            ConfirmTicketRequest("Ticket", message, dueDate, category, customerRepository.customer?.id),
        )

    companion object {
        private const val CONFIRM_TICKET_ENDPOINT = "app/agents/ticket"
        private const val GET_TICKET_CATEGORIES_ENDPOINT = "tickets/api/labels"
    }
}

/*

package com.inensus.feature_ticket.ticket_form.service

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.feature_ticket.TicketService
import com.inensus.feature_ticket.ticket_form.model.ConfirmTicketRequest
import com.inensus.shared_customer.repository.CustomerRepository
import java.util.*

class TicketFormRepository(
    private val service: TicketService,
    private val customerRepository: CustomerRepository,
    private val preferences: SharedPreferenceWrapper
) {
    var message: String? = null
    var dueDate: Date? = null
    var category: Long? = null

    fun getCategories() = service.getCategories()

    fun confirmTicket() =
        service.confirmTicket(
            preferences.baseUrl?.substring(0, preferences.baseUrl?.length?.minus(4) ?: 0) + CONFIRM_TICKET_ENDPOINT,
            ConfirmTicketRequest("Ticket", message, dueDate, category, customerRepository.customer?.id)
        )
}

 */
