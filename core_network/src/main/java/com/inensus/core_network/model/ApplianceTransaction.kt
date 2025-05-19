package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.Date

@Parcelize
data class ApplianceTransaction(
    @SerializedName("id") val id: Long,
    @SerializedName("rate_count") val tenure: Int,
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("total_cost") val cost: BigDecimal,
    @SerializedName("down_payment") val downPayment: BigDecimal,
    @SerializedName("asset_type") val applianceType: ApplianceType,
    @SerializedName("rates") val rates: List<Rate>,
    @SerializedName("person") val person: Person,
) : Parcelable

@Parcelize
data class Rate(
    @SerializedName("rate_cost") val rateAmount: BigDecimal,
    @SerializedName("remaining") val remainingAmount: BigDecimal,
    @SerializedName("due_date") val dueDate: String,
) : Parcelable

@Parcelize
data class Person(
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
) : Parcelable
