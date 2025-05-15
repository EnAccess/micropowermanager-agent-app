package com.inensus.feature_ticket.ticket_detail.di

import com.inensus.feature_ticket.ticket_detail.repository.TicketDetailRepository
import com.inensus.feature_ticket.ticket_detail.view.TicketDetailCreator
import com.inensus.feature_ticket.ticket_detail.viewmodel.TicketDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object TicketDetailModule {
    fun createTicketDetailModule() =
        module {
            viewModel { TicketDetailViewModel(get(), get()) }
            single { TicketDetailCreator(get()) }
            single { TicketDetailRepository(get(), get()) }
        }
}
