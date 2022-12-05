package com.inensus.feature_customers.customer_detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inensus.core_network.model.Customer
import com.inensus.shared_customer.repository.CustomerRepository

class CustomerDetailViewModel(private val repository: CustomerRepository) : ViewModel() {

    private var _customer: MutableLiveData<Customer> = MutableLiveData()
    val customer: LiveData<Customer> = _customer

    init {
        _customer.value = repository.customer
    }

    override fun onCleared() {
        super.onCleared()
        repository.clearCustomer()
    }
}