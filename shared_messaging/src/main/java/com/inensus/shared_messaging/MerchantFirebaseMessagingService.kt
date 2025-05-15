package com.inensus.shared_messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.inensus.core.broadcast.MessagingBroadcastReceiver
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.shared_navigation.SharedNavigation
import org.koin.android.ext.android.inject
import timber.log.Timber

class MerchantFirebaseMessagingService : FirebaseMessagingService() {
    private val navigation: SharedNavigation by inject()
    private val preferences: SharedPreferenceWrapper by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Timber.d("Refreshed token: $token")

        preferences.resetFirebase = true
        preferences.firebaseDeviceToken = token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        sendBroadcast(Intent(MessagingBroadcastReceiver.MESSAGING_ACTION))

        message.data.apply {
            displayNotification(get(EXTRA_ID), get(EXTRA_STATUS), get(EXTRA_PAYLOAD))
        }
    }

    private fun displayNotification(
        id: String?,
        status: String?,
        payload: String?,
    ) {
        val title = if (status == "1") "Successful Payment!" else "Payment Failed!"
        val content = if (status == "1") "Payment is processed succesfully" else "Payment has not been processed"

        val intent =
            navigation.createMessagingIntent(this).also {
                if (status == "1") {
                    it.putExtra(EXTRA_PAYLOAD, payload)
                }
            }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val style = NotificationCompat.BigTextStyle()
        style.setBigContentTitle(title)
        style.setSummaryText(content)

        val notificationBuilder =
            NotificationCompat
                .Builder(this, getString(R.string.notification_channel_id))
                .setSmallIcon(if (status == "1") R.drawable.ic_notification_success else R.drawable.ic_notification_error)
                .setStyle(style)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setColor(
                    if (status ==
                        "1"
                    ) {
                        ContextCompat.getColor(this, R.color.green_00C853)
                    } else {
                        ContextCompat.getColor(this, R.color.red_FF3B30)
                    },
                ).setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    getString(R.string.notification_channel_id),
                    getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    description = getString(R.string.notification_channel_description)
                    enableLights(true)
                    lightColor =
                        if (status == "1") {
                            ContextCompat.getColor(
                                this@MerchantFirebaseMessagingService,
                                R.color.green_00C853,
                            )
                        } else {
                            ContextCompat.getColor(this@MerchantFirebaseMessagingService, R.color.red_FF3B30)
                        }
                    enableVibration(true)
                }

            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(id?.toIntOrNull() ?: 0, notificationBuilder.build())
    }

    companion object {
        private const val EXTRA_ID = "id"
        private const val EXTRA_STATUS = "firebase_notification_status"
        private const val EXTRA_PAYLOAD = "payload"
    }
}
