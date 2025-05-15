package com.inensus.shared_agent.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Agent(
    @SerializedName("email") val email: String,
    @SerializedName("balance") val balance: BigDecimal,
)
