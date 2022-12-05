package com.inensus.feature_appliance.appliance_form.viewmodel

import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_network.toServiceError
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_appliance.appliance_form.repository.ApplianceFormRepository
import com.inensus.feature_appliance.appliance_form.view.ApplianceSummaryUiState
import io.reactivex.android.schedulers.AndroidSchedulers

class ApplianceSummaryViewModel(private val repository: ApplianceFormRepository) : BaseViewModel() {

    private val _uiState = LiveEvent<ApplianceSummaryUiState>()
    var uiState: LiveEvent<ApplianceSummaryUiState> = _uiState

    fun onConfirmButtonTapped() {
        showLoading()

        repository.confirmAppliance()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                _uiState.postValue(ApplianceSummaryUiState.OpenSuccess)
            }, {
                handleError(it.toServiceError())
            })
            .addTo(compositeDisposable)
    }
}
