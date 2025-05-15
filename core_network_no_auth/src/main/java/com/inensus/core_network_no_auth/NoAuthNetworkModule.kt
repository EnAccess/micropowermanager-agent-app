package com.inensus.core_network_no_auth

import com.inensus.core_network.di.InterceptorsModel
import com.inensus.core_network.di.Qualifiers
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit

object NoAuthNetworkModule {
    fun createNoAuthModule() =
        module {
            single(qualifier = NoAuthQualifiers.NO_AUTH_RETROFIT) {
                get<Retrofit>(
                    qualifier = Qualifiers.BASE_RETROFIT,
                    parameters = { parametersOf(provideInterceptors()) },
                )
            }
        }

    private fun provideInterceptors(): InterceptorsModel =
        InterceptorsModel(
            interceptors = emptyList(),
            networkInterceptors = emptyList(),
        )
}
