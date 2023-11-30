package com.inensus.feature_payment.payment_form.viewmodel

import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_network.toServiceError
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_payment.payment_form.service.PaymentFormRepository
import com.inensus.feature_payment.payment_form.view.PaymentSummaryUiState
import io.reactivex.android.schedulers.AndroidSchedulers

class PaymentSummaryViewModel(private val repository: PaymentFormRepository) : BaseViewModel() {

    private val _uiState = LiveEvent<PaymentSummaryUiState>()
    var uiState: LiveEvent<PaymentSummaryUiState> = _uiState

    fun onConfirmButtonTapped() {

        showLoading()

        repository.confirmPayment()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                _uiState.postValue(PaymentSummaryUiState.OpenSuccess)
            }, {
                handleError(it.toServiceError())
            })
            .addTo(compositeDisposable)
    }

}