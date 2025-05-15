package com.inensus.feature_customers.customer_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_network.model.Customer
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_customers.customer_list.service.CustomersRepository
import com.inensus.feature_customers.customer_list.view.CustomerListUiState
import com.inensus.feature_customers.customer_list.view.LoadCustomerType
import com.inensus.shared_customer.repository.CustomerRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import java.util.concurrent.TimeUnit

open class CustomersViewModel(
    private val repository: CustomersRepository,
    private val customerRepository: CustomerRepository,
) : BaseViewModel() {
    private val _uiState = LiveEvent<CustomerListUiState>()
    val uiState: LiveEvent<CustomerListUiState> = _uiState

    private val _customers = MutableLiveData<List<Customer>>()
    val customers: LiveData<List<Customer>> = _customers

    private val _searchTerm = repository.searchTerm
    val searchTerm: LiveData<String> = _searchTerm

    private val pagination = PublishProcessor.create<String>()

    private var loadCustomerType = LoadCustomerType.INITIAL

    init {
        observeCustomersPublish()

        getCustomers(type = LoadCustomerType.INITIAL)
    }

    private fun observeCustomersPublish() {
        pagination
            .debounce(PAGING_DEBOUNCE, TimeUnit.MILLISECONDS)
            .doOnNext {
                _uiState.postValue(CustomerListUiState.Loading(loadCustomerType))
            }.flatMapSingle {
                if (loadCustomerType == LoadCustomerType.PAGINATE) {
                    repository.getCustomers(loadCustomerType)
                } else {
                    repository.searchCustomer(it, loadCustomerType)
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _uiState.value = it
            }.addTo(compositeDisposable)
    }

    fun getCustomers(
        searchTerm: String = "",
        type: LoadCustomerType,
    ) {
        loadCustomerType = type

        pagination.onNext(searchTerm)
    }

    fun onCustomerTapped(customer: Customer) {
        customerRepository.customer = customer

        _uiState.value = CustomerListUiState.CustomerTapped
    }

    fun saveCustomersState(customers: List<Customer>) {
        _customers.value = customers
    }

    companion object {
        private const val PAGING_DEBOUNCE = 300L
    }
}
