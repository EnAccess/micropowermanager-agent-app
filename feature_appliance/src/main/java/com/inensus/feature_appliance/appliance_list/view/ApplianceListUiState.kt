package com.inensus.feature_appliance.appliance_list.view

import com.inensus.core_network.model.ApplianceTransaction

sealed class ApplianceListUiState {
    data class Loading(val type: LoadingApplianceListType) : ApplianceListUiState()
    data class Success(val appliances: List<ApplianceTransaction>, val type: LoadingApplianceListType) : ApplianceListUiState()
    object NoMoreData : ApplianceListUiState()
    object Error : ApplianceListUiState()
    object Empty : ApplianceListUiState()
    data class ApplianceTapped(val appliance: ApplianceTransaction) : ApplianceListUiState()
}