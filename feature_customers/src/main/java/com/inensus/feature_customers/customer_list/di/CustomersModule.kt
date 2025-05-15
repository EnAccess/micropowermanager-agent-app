package com.inensus.feature_customers.customer_list.di

import com.inensus.core_network_auth.AuthQualifiers
import com.inensus.feature_customers.customer_detail.viewmodel.CustomerDetailViewModel
import com.inensus.feature_customers.customer_list.service.CustomersRepository
import com.inensus.feature_customers.customer_list.service.CustomersService
import com.inensus.feature_customers.customer_list.viewmodel.CustomersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

object CustomersModule {
    fun createCustomersModules(): List<Module> =
        listOf(
            module { viewModel { CustomersViewModel(get(), get()) } },
            module { viewModel { CustomerDetailViewModel(get()) } },
            createCustomersNetworkModule(),
        )

    private fun createCustomersNetworkModule() =
        module {
            single { provideCustomersService(get(qualifier = AuthQualifiers.AUTH_RETROFIT)) }
            single { CustomersRepository(get(), get()) }
        }

    private fun provideCustomersService(retrofitClient: Retrofit) = retrofitClient.create(CustomersService::class.java)
}
