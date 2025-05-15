package com.inensus.feature_appliance.appliance_list.model

import com.google.gson.annotations.SerializedName
import com.inensus.core_network.model.Appliance

data class ApplianceTypeResponse(
    @SerializedName("data") val data: List<Appliance>,
    @SerializedName("next_page_url") val nextPageUrl: String,
)
