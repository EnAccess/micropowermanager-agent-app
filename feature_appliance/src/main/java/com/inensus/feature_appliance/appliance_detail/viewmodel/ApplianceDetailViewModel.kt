package com.inensus.feature_appliance.appliance_detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inensus.core_network.model.ApplianceTransaction
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_appliance.appliance_detail.view.ApplianceDetailCreator
import java.math.BigDecimal

class ApplianceDetailViewModel(
    private val detailCreator: ApplianceDetailCreator,
) : ViewModel() {
    private var _applianceDetails: MutableLiveData<List<KeyValue>> = MutableLiveData()
    val applianceDetails: LiveData<List<KeyValue>> = _applianceDetails

    private var _paymentDetails: MutableLiveData<Boolean> = MutableLiveData()
    val paymentDetails: LiveData<Boolean> = _paymentDetails

    fun getApplianceDetail(appliance: ApplianceTransaction) {
        _applianceDetails.value = detailCreator.createDetailItems(appliance)
    }

    fun checkPaymentDetails(appliance: ApplianceTransaction) {
        _paymentDetails.value = appliance.rates.any { it.rateAmount - it.remainingAmount > BigDecimal.ZERO }
    }
}
