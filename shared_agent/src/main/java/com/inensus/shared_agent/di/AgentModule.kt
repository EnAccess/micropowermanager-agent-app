package com.inensus.shared_agent.di

import com.inensus.core_network_auth.AuthQualifiers
import com.inensus.shared_agent.service.AgentRepository
import com.inensus.shared_agent.service.AgentService
import org.koin.dsl.module
import retrofit2.Retrofit

object AgentModule {
    fun createAgentModule() =
        module {
            single { provideAgentService(get(qualifier = AuthQualifiers.AUTH_RETROFIT)) }

            single { AgentRepository(get(), get()) }
        }

    private fun provideAgentService(retrofitClient: Retrofit) = retrofitClient.create(AgentService::class.java)
}
