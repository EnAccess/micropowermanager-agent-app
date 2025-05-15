package com.inensus.merchant.di

import com.inensus.core.broadcast.SessionExpireBroadcastReceiver
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.merchant.navigation.SharedNavigationImpl
import com.inensus.shared_navigation.SharedNavigation
import org.koin.dsl.module

object ApplicationModule {
    fun createAppModule() =
        module {
            single<SharedNavigation> { SharedNavigationImpl() }
            single { SessionExpireBroadcastReceiver() }
            single { SharedPreferenceWrapper(get()) }
        }
}
