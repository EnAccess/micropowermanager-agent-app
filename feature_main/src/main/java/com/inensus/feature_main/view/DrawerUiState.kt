package com.inensus.feature_main.view

import java.math.BigDecimal

sealed class DrawerUiState {
    object AgentLoading : DrawerUiState()

    object AgentError : DrawerUiState()

    class AgentLoaded(
        val email: String,
        val balance: BigDecimal,
    ) : DrawerUiState()
}
