package com.inensus.feature_ticket.ticket_form.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_ticket.ticket_detail.model.TicketCategory
import com.inensus.feature_ticket.ticket_form.service.TicketFormRepository
import com.inensus.feature_ticket.ticket_form.view.TicketFormUiState
import com.inensus.feature_ticket.ticket_form.view.TicketFormValidator
import com.inensus.feature_ticket.ticket_form.view.TicketSummaryCreator
import com.inensus.shared_customer.repository.CustomerRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.Date

class TicketFormViewModel(
    private val repository: TicketFormRepository,
    private val customerRepository: CustomerRepository,
    private val summaryCreator: TicketSummaryCreator,
    private val validator: TicketFormValidator,
) : BaseViewModel() {
    private val _uiState = LiveEvent<TicketFormUiState>()
    var uiState: LiveEvent<TicketFormUiState> = _uiState

    private val _categories = MutableLiveData<List<TicketCategory>>()
    var categories: LiveData<List<TicketCategory>> = _categories

    private val _message = MutableLiveData<String>()
    var message: LiveData<String> = _message

    private val _dueDate = MutableLiveData<Date>()
    var dueDate: LiveData<Date> = _dueDate

    private val _category = MutableLiveData<TicketCategory>()
    var category: LiveData<TicketCategory> = _category

    init {
        getCategories()
    }

    fun onMessageChanged(message: String) {
        _message.value = message
    }

    fun onDueDateChanged(dueDate: Date?) {
        _dueDate.value = dueDate
    }

    fun onCategoryChanged(category: String) {
        _category.value = _categories.value?.first { it.name == category }
    }

    fun onContinueButtonTapped() {
        validator.validateForm(_message.value, _category.value).let {
            if (it.isEmpty()) {
                repository.apply {
                    message = _message.value
                    dueDate = _dueDate.value
                    category = _category.value?.id
                }

                _uiState.value =
                    TicketFormUiState.OpenSummary(
                        summaryCreator.createSummaryItems(
                            customerRepository.customer?.name ?: "",
                            customerRepository.customer?.surname ?: "",
                            _message.value!!,
                            _dueDate.value,
                            _category.value?.name!!,
                        ),
                    )
            } else {
                _uiState.value = TicketFormUiState.ValidationError(it)
            }
        }
    }

    private fun getCategories() {
        repository
            .getCategories()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _categories.value = it.data
            }, {
                Timber.e(it)
            })
            .addTo(compositeDisposable)
    }
}
