package com.inensus.feature_ticket.ticket_form.viewmodel

import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_network.toServiceError
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_ticket.ticket_form.service.TicketFormRepository
import com.inensus.feature_ticket.ticket_form.view.TicketSummaryUiState
import io.reactivex.android.schedulers.AndroidSchedulers

class TicketSummaryViewModel(private val repository: TicketFormRepository) : BaseViewModel() {

    private val _uiState = LiveEvent<TicketSummaryUiState>()
    var uiState: LiveEvent<TicketSummaryUiState> = _uiState

    fun onConfirmButtonTapped() {
        showLoading()

        repository.confirmTicket()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                _uiState.value = TicketSummaryUiState.OpenSuccess
            }, {
                handleError(it.toServiceError())
            })
            .addTo(compositeDisposable)
    }
}