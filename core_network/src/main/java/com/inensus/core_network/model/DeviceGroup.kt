package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceGroup(@SerializedName("id") val id: Int, @SerializedName("device_serial") val deviceSerial: String, @SerializedName("device") val device: Device) : Parcelable