package com.inensus.feature_login.di

import com.inensus.core_network_no_auth.NoAuthQualifiers
import com.inensus.feature_login.service.LoginRepository
import com.inensus.feature_login.service.LoginService
import com.inensus.feature_login.view.LoginFormValidator
import com.inensus.feature_login.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

object LoginModule {

    fun createLoginModule() = module {
        single { LoginFormValidator() }
        viewModel { LoginViewModel(get(), get(), get()) }
    } + createLoginNetworkModule()

    private fun createLoginNetworkModule() = module {
        single { provideLoginService(get(qualifier = NoAuthQualifiers.NO_AUTH_RETROFIT)) }

        single { LoginRepository(get(), get()) }
    }

    private fun provideLoginService(retrofitClient: Retrofit) = retrofitClient.create(LoginService::class.java)
}