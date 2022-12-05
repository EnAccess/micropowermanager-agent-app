package com.inensus.feature_appliance.appliance_form.repository

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core_network.model.Appliance
import com.inensus.feature_appliance.ApplianceService
import com.inensus.feature_appliance.appliance_form.model.ConfirmApplianceRequest
import com.inensus.shared_customer.repository.CustomerRepository
import java.math.BigDecimal
import java.util.*

class ApplianceFormRepository(
    private val service: ApplianceService,
    private val customerRepository: CustomerRepository,
    private val preferences: SharedPreferenceWrapper
) {
    var appliances: List<Appliance> = emptyList()
    var appliance: Appliance? = null
    var downPayment: BigDecimal? = null
    var firstPaymentDate: Date? = null
    var tenure: Int? = null

    fun getAppliancesTypes() = service.getApplianceTypes(preferences.baseUrl + GET_APPLIANCE_TYPES_ENDPOINT).doOnSuccess { appliances = it.data }

    fun confirmAppliance() = service.confirmAppliance(
        preferences.baseUrl + CONFIRM_APPLIANCE_ENDPOINT,
        ConfirmApplianceRequest(
            customerRepository.customer?.id,
            appliance?.id,
            downPayment,
            firstPaymentDate,
            tenure
        )
    )

    companion object {
        private const val GET_APPLIANCE_TYPES_ENDPOINT = "app/agents/appliance_types"
        private const val CONFIRM_APPLIANCE_ENDPOINT = "app/agents/appliances"
    }
}