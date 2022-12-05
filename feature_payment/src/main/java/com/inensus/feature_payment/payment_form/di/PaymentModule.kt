package com.inensus.feature_payment.payment_form.di

import com.inensus.core_network_auth.AuthQualifiers
import com.inensus.feature_payment.payment_form.service.PaymentFormRepository
import com.inensus.feature_payment.payment_form.service.PaymentService
import com.inensus.feature_payment.payment_form.view.PaymentFormValidator
import com.inensus.feature_payment.payment_form.view.PaymentSummaryCreator
import com.inensus.feature_payment.payment_form.viewmodel.PaymentFormViewModel
import com.inensus.feature_payment.payment_form.viewmodel.PaymentSummaryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

object PaymentModule {
    fun createPaymentModules(): List<Module> = listOf(
        module {
            viewModel { PaymentFormViewModel(get(), get(), get(), get()) }
            viewModel { PaymentSummaryViewModel(get()) }
            single { PaymentSummaryCreator(get()) }
            single { PaymentFormValidator() }
        },
        createPaymentNetworkModule()
    )

    private fun createPaymentNetworkModule() = module {
        single { providePaymentService(get(qualifier = AuthQualifiers.AUTH_RETROFIT)) }

        single { PaymentFormRepository(get(), get()) }
    }

    private fun providePaymentService(retrofitClient: Retrofit) = retrofitClient.create(PaymentService::class.java)
}