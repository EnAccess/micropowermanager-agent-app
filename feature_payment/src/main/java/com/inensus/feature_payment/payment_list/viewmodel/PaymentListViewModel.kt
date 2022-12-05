package com.inensus.feature_payment.payment_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_network.model.Customer
import com.inensus.core_network.model.Payment
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_payment.payment_list.di.PaymentListModule
import com.inensus.feature_payment.payment_list.service.PaymentListRepository
import com.inensus.feature_payment.payment_list.view.LoadingPaymentListType
import com.inensus.feature_payment.payment_list.view.PaymentListUiState
import com.inensus.shared_customer.repository.CustomerRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import org.koin.java.KoinJavaComponent.getKoin

class PaymentListViewModel(private val repository: PaymentListRepository, private val customerRepository: CustomerRepository) : BaseViewModel() {

    private val _uiState = LiveEvent<PaymentListUiState>()
    val uiState: LiveEvent<PaymentListUiState> = _uiState

    private val _payments = MutableLiveData<List<Payment>>()
    val payments: LiveData<List<Payment>> = _payments

    private val _customer = MutableLiveData(customerRepository.customer)
    var customer: LiveData<Customer?> = _customer

    private val pagination = PublishProcessor.create<Unit>()
    private var loadingPaymentListType = LoadingPaymentListType.INITIAL

    init {
        observePaymentsPublish()

        getPayments(type = LoadingPaymentListType.INITIAL)
    }

    private fun observePaymentsPublish() {
        pagination
            .filter { uiState.value !is PaymentListUiState.Loading }
            .doOnNext {
                _uiState.postValue(PaymentListUiState.Loading(loadingPaymentListType))
            }
            .flatMapSingle {
                repository.getPayments(loadingPaymentListType, customerRepository.customer)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _uiState.value = it
            }
            .addTo(compositeDisposable)
    }

    fun getPayments(type: LoadingPaymentListType) {
        loadingPaymentListType = type

        pagination.onNext(Unit)
    }

    fun onPaymentTapped(payment: Payment) {
        _uiState.value = PaymentListUiState.PaymentTapped(payment.id)
    }

    fun savePaymentsState(payments: List<Payment>) {
        _payments.value = payments
    }

    override fun onCleared() {
        super.onCleared()
        getKoin().getScope(PaymentListModule.PAYMENT_LIST_SCOPE).close()
    }
}