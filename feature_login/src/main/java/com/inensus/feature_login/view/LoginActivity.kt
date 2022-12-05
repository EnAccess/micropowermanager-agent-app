package com.inensus.feature_login.view

import android.os.Bundle
import androidx.navigation.findNavController
import com.inensus.core_ui.BaseActivity
import com.inensus.feature_login.R

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun provideNavController() = findNavController(R.id.mainFragment)
}
