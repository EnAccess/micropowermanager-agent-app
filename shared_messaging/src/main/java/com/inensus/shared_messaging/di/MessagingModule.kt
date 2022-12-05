package com.inensus.shared_messaging.di

import com.inensus.core_network_auth.AuthQualifiers
import com.inensus.core.broadcast.MessagingBroadcastReceiver
import com.inensus.shared_messaging.NotificationHandler
import com.inensus.shared_messaging.repository.MessagingRepository
import com.inensus.shared_messaging.repository.MessagingService
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

object MessagingModule {
    fun createMessagingModules(): List<Module> = listOf(
        module {
            single { MessagingBroadcastReceiver() }
            single { NotificationHandler(get()) }
            single { MessagingRepository(get(), get()) }
        }, createMessagingNetworkModule()
    )

    private fun createMessagingNetworkModule() = module {
        single { providePaymentService(get(qualifier = AuthQualifiers.AUTH_RETROFIT)) }
    }

    private fun providePaymentService(retrofitClient: Retrofit) = retrofitClient.create(MessagingService::class.java)
}