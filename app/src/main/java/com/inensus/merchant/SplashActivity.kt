package com.inensus.merchant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.shared_navigation.Feature
import com.inensus.shared_navigation.SharedNavigation
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {
    private val navigation: SharedNavigation by inject()
    private val sharedPreferenceWrapper: SharedPreferenceWrapper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (sharedPreferenceWrapper.accessToken.isEmpty()) {
            navigation.navigateTo(this, Feature.Login)
        } else {
            navigation.navigateTo(this, Feature.Main)
        }
    }
}
