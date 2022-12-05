package com.inensus.feature_appliance.appliance_list.model

import com.google.gson.annotations.SerializedName
import com.inensus.core_network.model.ApplianceTransaction

data class ApplianceResponse(
    @SerializedName("data") val data: List<ApplianceTransaction>,
    @SerializedName("next_page_url") val nextPageUrl: String
)