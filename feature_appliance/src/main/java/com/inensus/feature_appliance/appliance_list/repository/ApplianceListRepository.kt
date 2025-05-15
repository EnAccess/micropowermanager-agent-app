package com.inensus.feature_appliance.appliance_list.repository

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core_network.model.Customer
import com.inensus.feature_appliance.ApplianceService
import com.inensus.feature_appliance.appliance_list.view.ApplianceListUiState
import com.inensus.feature_appliance.appliance_list.view.LoadingApplianceListType
import io.reactivex.Single
import timber.log.Timber

class ApplianceListRepository(
    private val service: ApplianceService,
    private val preferences: SharedPreferenceWrapper,
) {
    private val initialUrl = preferences.baseUrl + "app/agents/appliances"

    private var url: String? = initialUrl

    fun getAppliances(
        loadingApplianceListType: LoadingApplianceListType,
        customer: Customer?,
    ): Single<ApplianceListUiState> {
        if (loadingApplianceListType == LoadingApplianceListType.INITIAL) {
            url = initialUrl
        }

        url?.let {
            if (url == initialUrl && customer != null) {
                url += "/" + customer.id
            }

            return service
                .getAppliances(url!!)
                .map { response ->
                    if (response.data.isEmpty() && url?.contains("page") == false) {
                        ApplianceListUiState.Empty
                    } else {
                        url = response.nextPageUrl
                        ApplianceListUiState.Success(response.data, loadingApplianceListType)
                    }
                }.doOnError { error ->
                    Timber.e(error)
                }.onErrorResumeNext { Single.just(ApplianceListUiState.Error) }
        } ?: let {
            return Single.just(ApplianceListUiState.NoMoreData)
        }
    }
}
