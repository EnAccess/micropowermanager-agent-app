package com.inensus.feature_payment.payment_graph.di

import com.inensus.core_network_auth.AuthQualifiers
import com.inensus.feature_payment.payment_graph.service.PaymentGraphRepository
import com.inensus.feature_payment.payment_graph.service.PaymentGraphService
import com.inensus.feature_payment.payment_graph.view.PaymentGraphModelConverter
import com.inensus.feature_payment.payment_graph.viewmodel.PaymentGraphViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

object PaymentGraphModule {
    fun createPaymentGraphModule() =
        module {
            viewModel { PaymentGraphViewModel(get(), get(), get()) }

            single { PaymentGraphRepository(get(), get()) }
            single { PaymentGraphModelConverter() }
            single { providePaymentGraphService(get(qualifier = AuthQualifiers.AUTH_RETROFIT)) }
        }

    private fun providePaymentGraphService(retrofitClient: Retrofit) = retrofitClient.create(PaymentGraphService::class.java)
}
