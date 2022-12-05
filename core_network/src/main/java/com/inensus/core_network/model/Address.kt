package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(@SerializedName("phone") val phone: String, @SerializedName("city") val city: City): Parcelable