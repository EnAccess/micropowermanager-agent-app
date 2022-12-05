package com.inensus.merchant.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.inensus.feature_login.view.LoginActivity
import com.inensus.feature_main.view.MainActivity
import com.inensus.merchant.SplashActivity
import com.inensus.shared_navigation.Feature
import com.inensus.shared_navigation.SharedNavigation

class SharedNavigationImpl : SharedNavigation {
    override fun navigateTo(from: Activity, feature: Feature) {
        val toActivity: Class<*> = when (feature) {
            Feature.Main -> {
                MainActivity::class.java
            }
            Feature.Login -> {
                LoginActivity::class.java
            }
            Feature.Splash -> {
                SplashActivity::class.java
            }
        }

        with(from) {
            startActivity(Intent(this, toActivity).apply {
                putExtras(intent)
            })
            finish()
        }
    }

    override fun createMessagingIntent(context: Context) =
        Intent(context, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
}