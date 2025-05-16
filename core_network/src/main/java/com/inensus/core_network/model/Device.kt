package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Device(
    @SerializedName("id") val id: Int,
    @SerializedName("serial_number") val serialNumber: String,
) : Parcelable
