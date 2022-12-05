package com.inensus.feature_payment.payment_list.service

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core_network.model.Customer
import com.inensus.feature_payment.payment_form.service.PaymentService
import com.inensus.feature_payment.payment_list.view.LoadingPaymentListType
import com.inensus.feature_payment.payment_list.view.PaymentListUiState
import io.reactivex.Single
import timber.log.Timber

class PaymentListRepository(private val service: PaymentService, preferences: SharedPreferenceWrapper) {

    private val initialUrl = preferences.baseUrl + "app/agents/transactions"
    private var url: String? = initialUrl

    fun getPayments(loadingPaymentListType: LoadingPaymentListType, customer: Customer?): Single<PaymentListUiState> {
        if (loadingPaymentListType == LoadingPaymentListType.INITIAL) {
            url = initialUrl
        }

        url?.let {
            if (url == initialUrl && customer != null) {
                url += "/" + customer.id
            }

            return service.getPayments(url!!)
                .map { response ->
                    if (response.data.isEmpty() && url?.contains("page") == false) {
                        PaymentListUiState.Empty
                    } else {
                        url = response.nextPageUrl
                        PaymentListUiState.Success(response.data, loadingPaymentListType)
                    }
                }
                .doOnError { error ->
                    Timber.e(error)
                }
                .onErrorResumeNext { Single.just(PaymentListUiState.Error) }
        } ?: let {
            return Single.just(PaymentListUiState.NoMoreData)
        }
    }
}