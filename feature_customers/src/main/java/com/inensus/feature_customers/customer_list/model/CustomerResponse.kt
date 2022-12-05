package com.inensus.feature_customers.customer_list.model

import com.google.gson.annotations.SerializedName
import com.inensus.core_network.model.Customer

data class CustomerResponse(
    @SerializedName("next_page_url") val nextPageUrl: String,
    @SerializedName("data") val data: List<Customer>
)