package com.inensus.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.inensus.core.lifecycle.LiveEvent

class SessionExpireBroadcastReceiver : BroadcastReceiver() {
    val event = LiveEvent<Unit>()

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        event.postValue(Unit)
    }

    companion object {
        const val SESSION_EXPIRE_INTENT_ACTION = "sessionExpireIntentAction"
    }
}
