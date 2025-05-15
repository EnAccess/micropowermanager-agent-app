package com.inensus.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.inensus.core.lifecycle.LiveEvent

class MessagingBroadcastReceiver : BroadcastReceiver() {
    val event = LiveEvent<Nothing>()

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        event.postValue(null)
    }

    companion object {
        const val MESSAGING_ACTION = "MessagingAction"
    }
}
