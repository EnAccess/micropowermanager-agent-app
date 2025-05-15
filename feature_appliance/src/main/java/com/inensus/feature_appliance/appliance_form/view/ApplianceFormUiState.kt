package com.inensus.feature_appliance.appliance_form.view

import com.inensus.core_network.model.Appliance
import com.inensus.core_ui.key_value.KeyValue

sealed class ApplianceFormUiState {
    object Loading : ApplianceFormUiState()

    data class AppliancesLoaded(
        val appliances: List<Appliance>,
    ) : ApplianceFormUiState()

    object Error : ApplianceFormUiState()

    data class OpenSummary(
        val summaryItems: ArrayList<KeyValue>,
    ) : ApplianceFormUiState()

    data class ValidationError(
        val errors: List<Error>,
    ) : ApplianceFormUiState() {
        sealed class Error {
            object ApplianceIsBlank : Error()

            object ApplianceAmountIsMissing : Error()

            object FirstPaymentDateIsBlank : Error()

            object DownPaymentMoreThanAppliance : Error()

            object TenureIsBlank : Error()
        }
    }
}
