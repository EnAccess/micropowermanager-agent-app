package com.inensus.core_network_auth

import android.content.Context
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core_network.di.InterceptorsModel
import com.inensus.core_network.di.Qualifiers
import com.inensus.core_network_auth.interceptor.AuthorizationInterceptor
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit

object AuthNetworkModule {

    fun createAuthModule(): Module = module {
        single(qualifier = AuthQualifiers.AUTH_RETROFIT) {
            get<Retrofit>(
                qualifier = Qualifiers.BASE_RETROFIT,
                parameters = { parametersOf(provideInterceptors(get(), get())) }
            )
        }
    }

    private fun provideInterceptors(context: Context, sharedPreferenceWrapper: SharedPreferenceWrapper): InterceptorsModel = InterceptorsModel(
        interceptors = listOf(AuthorizationInterceptor(context, sharedPreferenceWrapper)),
        networkInterceptors = emptyList()
    )
}