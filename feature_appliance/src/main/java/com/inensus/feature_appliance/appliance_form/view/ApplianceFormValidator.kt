package com.inensus.feature_appliance.appliance_form.view

import com.inensus.core_network.model.Appliance
import java.math.BigDecimal
import java.util.*

class ApplianceFormValidator {
    fun validateForm(
        appliance: Appliance?,
        firstPaymentDate: Date?,
        downPayment: BigDecimal?,
        tenure: Int?,
    ): List<ApplianceFormUiState.ValidationError.Error> =
        mutableListOf<ApplianceFormUiState.ValidationError.Error>().apply {
            if (appliance == null) {
                add(ApplianceFormUiState.ValidationError.Error.ApplianceIsBlank)
            }
            if (appliance?.cost == null) {
                add(ApplianceFormUiState.ValidationError.Error.ApplianceAmountIsMissing)
            }
            if (firstPaymentDate == null) {
                add(ApplianceFormUiState.ValidationError.Error.FirstPaymentDateIsBlank)
            }
            if (downPayment != null && appliance != null && downPayment > appliance.cost) {
                add(ApplianceFormUiState.ValidationError.Error.DownPaymentMoreThanAppliance)
            }
            if (tenure == null) {
                add(ApplianceFormUiState.ValidationError.Error.TenureIsBlank)
            }
        }
}
