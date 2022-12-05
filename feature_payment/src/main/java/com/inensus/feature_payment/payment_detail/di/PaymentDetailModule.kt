package com.inensus.feature_payment.payment_detail.di

import com.inensus.feature_payment.payment_detail.repository.PaymentDetailRepository
import com.inensus.feature_payment.payment_detail.view.PaymentDetailCreator
import com.inensus.feature_payment.payment_detail.viewmodel.PaymentDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object PaymentDetailModule {
    fun createPaymentDetailModule() = module {
        viewModel { PaymentDetailViewModel(get(), get()) }
        single { PaymentDetailCreator(get()) }
        single { PaymentDetailRepository(get(), get()) }
    }
}