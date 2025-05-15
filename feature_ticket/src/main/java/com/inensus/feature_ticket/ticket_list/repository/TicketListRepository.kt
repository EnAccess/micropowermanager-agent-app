package com.inensus.feature_ticket.ticket_list.repository

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core_network.model.Customer
import com.inensus.feature_ticket.TicketService
import com.inensus.feature_ticket.ticket_list.view.LoadingTicketListType
import com.inensus.feature_ticket.ticket_list.view.TicketListUiState
import io.reactivex.Single
import timber.log.Timber

class TicketListRepository(
    private val service: TicketService,
    private val preferences: SharedPreferenceWrapper,
) {
    private val initialUrl = preferences.baseUrl + "app/agents/ticket"
    private var url: String? = initialUrl

    fun getTickets(
        loadingTicketListType: LoadingTicketListType,
        customer: Customer?,
    ): Single<TicketListUiState> {
        if (loadingTicketListType == LoadingTicketListType.INITIAL) {
            url = initialUrl
        }

        url?.let {
            if (url == initialUrl && customer != null) {
                url += "/customer/" + customer.id
            }

            return service
                .getTickets(url!!)
                .map { response ->
                    if (response.data.isEmpty() && url?.contains("page") == false) {
                        TicketListUiState.Empty
                    } else {
                        url = response.nextPageUrl
                        TicketListUiState.Success(response.data, loadingTicketListType)
                    }
                }.doOnError { error ->
                    Timber.e(error)
                }.onErrorResumeNext { Single.just(TicketListUiState.Error) }
        } ?: let {
            return Single.just(TicketListUiState.NoMoreData)
        }
    }
}
