package com.inensus.shared_navigation

import android.app.Activity
import android.content.Context
import android.content.Intent

interface SharedNavigation {
    fun navigateTo(from: Activity, feature: Feature)

    fun createMessagingIntent(context: Context): Intent
}