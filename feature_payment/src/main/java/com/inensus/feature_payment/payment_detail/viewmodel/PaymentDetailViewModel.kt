package com.inensus.feature_payment.payment_detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core_network.toServiceError
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_payment.payment_detail.repository.PaymentDetailRepository
import com.inensus.feature_payment.payment_detail.view.PaymentDetailCreator
import io.reactivex.android.schedulers.AndroidSchedulers

class PaymentDetailViewModel(
    private val repository: PaymentDetailRepository,
    private val detailCreator: PaymentDetailCreator,
) : BaseViewModel() {
    private var _paymentDetails: MutableLiveData<List<KeyValue>> = MutableLiveData()
    val paymentDetails: LiveData<List<KeyValue>> = _paymentDetails

    fun getPaymentDetail(paymentId: Long) {
        showLoading()

        repository
            .getPaymentDetail(paymentId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                _paymentDetails.value = detailCreator.createDetailItems(it.data)
            }, {
                handleError(it.toServiceError())
            })
            .addTo(compositeDisposable)
    }
}
