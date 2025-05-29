package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceGroup(
    @SerializedName("id") val id: Int,
    @SerializedName("device_serial") val deviceSerial: String,
    @SerializedName("device") val device: Device,
    @SerializedName("device_type") val deviceType: String,
) : Parcelable
