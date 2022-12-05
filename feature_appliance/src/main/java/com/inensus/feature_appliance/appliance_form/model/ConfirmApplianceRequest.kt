package com.inensus.feature_appliance.appliance_form.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.*

data class ConfirmApplianceRequest(
    @SerializedName("person_id") val customerId: Long?,
    @SerializedName("agent_assigned_appliance_id") val applianceId: Long?,
    @SerializedName("down_payment") val downPayment: BigDecimal?,
    @SerializedName("first_payment_date") val date: Date?,
    @SerializedName("tenure") val tenure: Int?
)