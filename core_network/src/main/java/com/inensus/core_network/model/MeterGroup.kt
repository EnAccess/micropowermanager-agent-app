package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MeterGroup(
    @SerializedName("id") val id: Int,
    @SerializedName("meter") val meter: Meter,
) : Parcelable
