package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class ApplianceRate(
    @SerializedName("rate_cost") val rateCost: BigDecimal,
    @SerializedName("remaining") val remaining: BigDecimal,
    @SerializedName("due_date") val dueDate: String,
) : Parcelable
