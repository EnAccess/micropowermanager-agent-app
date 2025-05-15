package com.inensus.feature_ticket.ticket_detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core_network.toServiceError
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_ticket.ticket_detail.repository.TicketDetailRepository
import com.inensus.feature_ticket.ticket_detail.view.TicketDetailCreator
import io.reactivex.android.schedulers.AndroidSchedulers

class TicketDetailViewModel(
    private val repository: TicketDetailRepository,
    private val detailCreator: TicketDetailCreator,
) : BaseViewModel() {
    private var _ticketDetails: MutableLiveData<List<KeyValue>> = MutableLiveData()
    val ticketDetails: LiveData<List<KeyValue>> = _ticketDetails

    fun getTicketDetail(ticketId: String) {
        showLoading()

        repository
            .getTicketDetail(ticketId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                _ticketDetails.value = detailCreator.createDetailItems(it.data)
            }, {
                handleError(it.toServiceError())
            })
            .addTo(compositeDisposable)
    }
}
