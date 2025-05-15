package com.inensus.feature_login.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("agent") val agent: Agent,
)

data class Agent(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("balance") val balance: BigDecimal,
)
