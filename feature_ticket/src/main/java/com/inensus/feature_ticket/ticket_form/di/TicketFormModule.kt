package com.inensus.feature_ticket.ticket_form.di

import com.inensus.core_network_auth.AuthQualifiers
import com.inensus.feature_ticket.TicketService
import com.inensus.feature_ticket.ticket_form.service.TicketFormRepository
import com.inensus.feature_ticket.ticket_form.view.TicketFormValidator
import com.inensus.feature_ticket.ticket_form.view.TicketSummaryCreator
import com.inensus.feature_ticket.ticket_form.viewmodel.TicketFormViewModel
import com.inensus.feature_ticket.ticket_form.viewmodel.TicketSummaryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

object TicketFormModule {
    fun createTicketModules(): List<Module> = listOf(
        module {
            viewModel { TicketFormViewModel(get(), get(), get(), get()) }
            viewModel { TicketSummaryViewModel(get()) }
            single { TicketSummaryCreator(get()) }
            single { TicketFormValidator() }
        },
        createTicketNetworkModule()
    )

    private fun createTicketNetworkModule() = module {
        single { provideTicketService(get(qualifier = AuthQualifiers.AUTH_RETROFIT)) }

        single { TicketFormRepository(get(), get(), get()) }
    }

    private fun provideTicketService(retrofitClient: Retrofit) = retrofitClient.create(TicketService::class.java)
}