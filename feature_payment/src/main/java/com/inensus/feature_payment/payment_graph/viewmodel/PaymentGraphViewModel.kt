package com.inensus.feature_payment.payment_graph.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_network.toServiceError
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_payment.payment_graph.model.PaymentGraphPeriod
import com.inensus.feature_payment.payment_graph.service.PaymentGraphRepository
import com.inensus.feature_payment.payment_graph.view.PaymentGraphModelConverter
import com.inensus.feature_payment.payment_graph.view.PaymentGraphUiState
import com.inensus.shared_customer.repository.CustomerRepository
import io.reactivex.android.schedulers.AndroidSchedulers

class PaymentGraphViewModel(
    private val repository: PaymentGraphRepository,
    private val customerRepository: CustomerRepository,
    private val converter: PaymentGraphModelConverter
) : BaseViewModel() {

    private var _period: MutableLiveData<PaymentGraphPeriod> = MutableLiveData()
    val period: LiveData<PaymentGraphPeriod> = _period

    private var _uiState = LiveEvent<PaymentGraphUiState>()
    val uiState: LiveEvent<PaymentGraphUiState> = _uiState

    init {
        _period.postValue(repository.graphPeriods.first())
    }

    fun onPeriodChanged(period: String) {
        _period.value = repository.graphPeriods.find { it.value == period }

        getPaymentGraphData()
    }

    private fun getPaymentGraphData() {
        showLoading()

        repository.getPaymentGraphData(customerRepository.customer?.id, _period.value?.key)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                _uiState.value = PaymentGraphUiState.Success(
                    repository.graphPeriods.map { period -> period.value },
                    converter.xAxisList,
                    converter.fromDataToUiModel(converter.fromJsonToData(it))
                )
            }, {
                handleError(it.toServiceError())
            })
            .addTo(compositeDisposable)
    }
}