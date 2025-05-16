package com.inensus.feature_payment.payment_form.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_network.model.Customer
import com.inensus.feature_payment.payment_form.service.PaymentFormRepository
import com.inensus.feature_payment.payment_form.view.PaymentFormUiState
import com.inensus.feature_payment.payment_form.view.PaymentFormValidator
import com.inensus.feature_payment.payment_form.view.PaymentSummaryCreator
import com.inensus.shared_customer.repository.CustomerRepository
import java.math.BigDecimal

class PaymentFormViewModel(
    private val repository: PaymentFormRepository,
    customerRepository: CustomerRepository,
    private val summaryCreator: PaymentSummaryCreator,
    private val validator: PaymentFormValidator,
) : ViewModel() {
    private val _uiState = LiveEvent<PaymentFormUiState>()
    var uiState: LiveEvent<PaymentFormUiState> = _uiState

    private val _customer = MutableLiveData(customerRepository.customer)
    var customer: LiveData<Customer?> = _customer

    private val _device = MutableLiveData<String>()
    var device: LiveData<String> = _device

    private val _amount = MutableLiveData<String>()
    var amount: LiveData<String> = _amount

    fun onDeviceChanged(device: String) {
        _device.value = device
    }

    fun onAmountChanged(amount: String) {
        _amount.value = amount
    }

    fun onContinueButtonTapped() {
        validator.validateForm(_device.value, _amount.value).let {
            if (it.isEmpty()) {
                repository.device = _device.value
                repository.amount = _amount.value

                _uiState.value =
                    PaymentFormUiState.OpenSummary(
                        summaryCreator.createSummaryItems(
                            _customer.value?.name ?: "",
                            _customer.value?.surname ?: "",
                            _device.value ?: "",
                            BigDecimal(_amount.value),
                        ),
                    )
            } else {
                _uiState.value = PaymentFormUiState.ValidationError(it)
            }
        }
    }
}
