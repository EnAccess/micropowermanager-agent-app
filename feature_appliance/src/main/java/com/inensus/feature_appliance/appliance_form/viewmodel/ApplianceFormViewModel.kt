package com.inensus.feature_appliance.appliance_form.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_network.model.Appliance
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_appliance.appliance_form.repository.ApplianceFormRepository
import com.inensus.feature_appliance.appliance_form.view.ApplianceFormUiState
import com.inensus.feature_appliance.appliance_form.view.ApplianceFormValidator
import com.inensus.feature_appliance.appliance_form.view.ApplianceSummaryCreator
import io.reactivex.android.schedulers.AndroidSchedulers
import java.math.BigDecimal
import java.util.*
import kotlin.math.min

class ApplianceFormViewModel(
    private val repository: ApplianceFormRepository,
    private val summaryCreator: ApplianceSummaryCreator,
    private val validator: ApplianceFormValidator
) : BaseViewModel() {

    private val _uiState = LiveEvent<ApplianceFormUiState>()
    var uiState: LiveEvent<ApplianceFormUiState> = _uiState

    private val _appliance = MutableLiveData<Appliance>()
    var appliance: LiveData<Appliance> = _appliance

    private val _firstPaymentDate = MutableLiveData<Date>()
    var firstPaymentDate: LiveData<Date> = _firstPaymentDate

    private val _downPayment = MutableLiveData<BigDecimal>()
    var downPayment: LiveData<BigDecimal> = _downPayment

    private val _tenure = MutableLiveData<Int>()
    var tenure: LiveData<Int> = _tenure

    private val _monthlyPaymentAmount = MutableLiveData<BigDecimal>()
    var monthlyPaymentAmount: LiveData<BigDecimal> = _monthlyPaymentAmount

    fun getAppliances() {
        if (repository.appliances.isEmpty()) {
            _uiState.postValue(ApplianceFormUiState.Loading)

            repository.getAppliancesTypes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _uiState.postValue(ApplianceFormUiState.AppliancesLoaded(it.data))
                }, {
                    _uiState.postValue(ApplianceFormUiState.Error)
                })
                .addTo(compositeDisposable)
        } else {
            _uiState.postValue(ApplianceFormUiState.AppliancesLoaded(repository.appliances))
        }
    }

    fun onApplianceChanged(applianceName: String) {
        _appliance.value = repository.appliances.first { it.type.name == applianceName }

        calculateMonthlyPaymentAmount()
    }

    fun onFirstPaymentDateChanged(date: Date?) {
        _firstPaymentDate.value = date
    }

    fun onDownPaymentChanged(downPayment: String) {
        _downPayment.value =
            if (downPayment.isEmpty()) null else min(downPayment.toInt(), _appliance.value?.cost?.intValueExact() ?: 0).toBigDecimal()

        calculateMonthlyPaymentAmount()
    }

    fun onTenureChanged(tenure: String) {
        _tenure.value = if (tenure.isEmpty()) null else tenure.toInt()

        calculateMonthlyPaymentAmount()
    }

    private fun calculateMonthlyPaymentAmount() {
        if (_appliance.value == null || _appliance.value?.cost == null || _tenure.value == null) {
            _monthlyPaymentAmount.value = BigDecimal.ZERO
        } else {
            if (_downPayment.value?.toInt() == _appliance.value?.cost?.toInt()) {
                _tenure.value = 1
            }

            val downPayment = _downPayment.value ?: BigDecimal.ZERO
            val amount = (_appliance.value?.cost?.subtract(downPayment))?.div(BigDecimal(_tenure.value!!))
            _monthlyPaymentAmount.value = amount
        }
    }

    fun onContinueButtonTapped() {
        validator.validateForm(_appliance.value, _firstPaymentDate.value, _downPayment.value, _tenure.value).let {
            if (it.isEmpty()) {
                repository.appliance = _appliance.value
                repository.downPayment = _downPayment.value
                repository.firstPaymentDate = _firstPaymentDate.value
                repository.tenure = _tenure.value

                _uiState.value = ApplianceFormUiState.OpenSummary(
                    summaryCreator.createSummaryItems(
                        _appliance.value!!,
                        _firstPaymentDate.value!!,
                        _downPayment.value,
                        _tenure.value!!,
                        _monthlyPaymentAmount.value!!
                    )
                )
            } else {
                _uiState.value = ApplianceFormUiState.ValidationError(it)
            }
        }
    }
}
