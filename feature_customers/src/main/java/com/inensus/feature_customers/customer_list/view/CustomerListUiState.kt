package com.inensus.feature_customers.customer_list.view

import com.inensus.core_network.model.Customer

sealed class CustomerListUiState {
    class Loading(val type: LoadCustomerType) : CustomerListUiState()
    class Success(val customers: List<Customer>, val type: LoadCustomerType) : CustomerListUiState()
    object NoMoreData : CustomerListUiState()
    object Error : CustomerListUiState()
    object Empty : CustomerListUiState()
    object CustomerTapped : CustomerListUiState()
}