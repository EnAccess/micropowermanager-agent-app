package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class Appliance(
    @SerializedName("id") val id: Long,
    @SerializedName("price") val cost: BigDecimal,
    @SerializedName("asset_type") val type: ApplianceType,
) : Parcelable

@Parcelize
data class ApplianceType(
    @SerializedName("name") val name: String,
) : Parcelable
