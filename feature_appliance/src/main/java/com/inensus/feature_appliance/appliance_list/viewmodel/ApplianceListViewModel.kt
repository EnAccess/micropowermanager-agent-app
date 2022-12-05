package com.inensus.feature_appliance.appliance_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_network.model.ApplianceTransaction
import com.inensus.core_network.model.Customer
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_appliance.appliance_list.di.ApplianceListModule
import com.inensus.feature_appliance.appliance_list.repository.ApplianceListRepository
import com.inensus.feature_appliance.appliance_list.view.ApplianceListUiState
import com.inensus.feature_appliance.appliance_list.view.LoadingApplianceListType
import com.inensus.shared_customer.repository.CustomerRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import org.koin.java.KoinJavaComponent.getKoin

class ApplianceListViewModel(private val repository: ApplianceListRepository, private val customerRepository: CustomerRepository) : BaseViewModel() {

    private val _uiState = LiveEvent<ApplianceListUiState>()
    val uiState: LiveEvent<ApplianceListUiState> = _uiState

    private val _appliances = MutableLiveData<List<ApplianceTransaction>>()
    val appliances: LiveData<List<ApplianceTransaction>> = _appliances

    private val _customer = MutableLiveData(customerRepository.customer)
    var customer: LiveData<Customer?> = _customer

    private val pagination = PublishProcessor.create<Unit>()
    private var loadingApplianceListType = LoadingApplianceListType.INITIAL

    init {
        observeAppliancesPublish()

        getAppliances(type = LoadingApplianceListType.INITIAL)
    }

    private fun observeAppliancesPublish() {
        pagination
            .filter { uiState.value !is ApplianceListUiState.Loading }
            .doOnNext {
                _uiState.postValue(ApplianceListUiState.Loading(loadingApplianceListType))
            }
            .flatMapSingle {
                repository.getAppliances(loadingApplianceListType, customerRepository.customer)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _uiState.value = it
            }
            .addTo(compositeDisposable)
    }

    fun getAppliances(type: LoadingApplianceListType) {
        loadingApplianceListType = type

        pagination.onNext(Unit)
    }

    fun onApplianceTapped(appliance: ApplianceTransaction) {
        _uiState.value = ApplianceListUiState.ApplianceTapped(appliance)
    }

    fun saveAppliancesState(appliances: List<ApplianceTransaction>) {
        _appliances.value = appliances
    }

    override fun onCleared() {
        super.onCleared()
        getKoin().getScope(ApplianceListModule.APPLIANCE_LIST_SCOPE).close()
    }
}