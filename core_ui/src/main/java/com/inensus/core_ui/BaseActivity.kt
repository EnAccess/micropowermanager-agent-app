package com.inensus.core_ui

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.inensus.core.broadcast.MessagingBroadcastReceiver
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {
    val messagingReceiver: MessagingBroadcastReceiver by inject()

    abstract fun provideNavController(): NavController
}
