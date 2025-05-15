package com.inensus.feature_payment.payment_list.view

import com.inensus.core_network.model.Payment

sealed class PaymentListUiState {
    data class Loading(
        val type: LoadingPaymentListType,
    ) : PaymentListUiState()

    data class Success(
        val payments: List<Payment>,
        val type: LoadingPaymentListType,
    ) : PaymentListUiState()

    object NoMoreData : PaymentListUiState()

    object Error : PaymentListUiState()

    object Empty : PaymentListUiState()

    data class PaymentTapped(
        val paymentId: Long,
    ) : PaymentListUiState()
}
