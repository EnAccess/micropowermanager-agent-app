package com.inensus.merchant

import android.app.Application
import com.inensus.core_network.di.CoreNetworkModule
import com.inensus.core_network_auth.AuthNetworkModule
import com.inensus.core_network_no_auth.NoAuthNetworkModule
import com.inensus.feature_login.di.LoginModule
import com.inensus.feature_main.di.MainModule
import com.inensus.merchant.di.ApplicationModule
import com.inensus.shared_agent.di.AgentModule
import com.inensus.shared_customer.di.CustomerModule
import com.inensus.shared_messaging.di.MessagingModule
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MerchantApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        RxJavaPlugins.setErrorHandler {
            Timber.d(it)
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MerchantApplication)
            modules(
                listOf(
                    ApplicationModule.createAppModule(),
                    CoreNetworkModule.createCoreNetworkModule(),
                    AuthNetworkModule.createAuthModule(),
                    NoAuthNetworkModule.createNoAuthModule(),
                    *LoginModule.createLoginModule().toTypedArray(),
                    *MainModule.createMainModules().toTypedArray(),
                    AgentModule.createAgentModule(),
                    CustomerModule.createCustomerModule(),
                    *MessagingModule.createMessagingModules().toTypedArray(),
                ),
            )
        }
    }
}
