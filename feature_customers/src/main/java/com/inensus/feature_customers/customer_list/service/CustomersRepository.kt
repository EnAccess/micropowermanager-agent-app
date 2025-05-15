package com.inensus.feature_customers.customer_list.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.feature_customers.customer_list.view.CustomerListUiState
import com.inensus.feature_customers.customer_list.view.LoadCustomerType
import io.reactivex.Single
import timber.log.Timber

class CustomersRepository(
    private val service: CustomersService,
    private val preferences: SharedPreferenceWrapper,
) {
    private val initialUrl = preferences.baseUrl + "app/agents/customers"
    private var url: String? = initialUrl

    private val _searchTerm = MutableLiveData<String>()
    val searchTerm: LiveData<String> = _searchTerm

    fun getCustomers(loadCustomerType: LoadCustomerType): Single<CustomerListUiState> =
        url?.let {
            if (_searchTerm.value?.isNotBlank() == true && url?.contains("term") == false && url?.contains("page") == true) {
                url += "&term=${_searchTerm.value}"
            }

            service
                .getCustomers(url!!)
                .map { response ->
                    if (response.data.isEmpty() && url?.contains("page") == false) {
                        CustomerListUiState.Empty
                    } else {
                        url = response.nextPageUrl
                        CustomerListUiState.Success(response.data, loadCustomerType)
                    }
                }.doOnError { error ->
                    Timber.e(error)
                }.onErrorResumeNext { Single.just(CustomerListUiState.Error) }
        } ?: let {
            return Single.just(CustomerListUiState.NoMoreData)
        }

    fun searchCustomer(
        searchTerm: String,
        loadCustomerType: LoadCustomerType,
    ): Single<CustomerListUiState> {
        _searchTerm.postValue(searchTerm)
        url = initialUrl

        if (searchTerm.isNotBlank()) {
            url += "/search?term=$searchTerm"
        }

        return getCustomers(loadCustomerType)
    }
}
