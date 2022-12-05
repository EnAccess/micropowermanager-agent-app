package com.inensus.shared_messaging.repository

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.shared_messaging.model.MessagingRequest

class MessagingRepository(private val service: MessagingService, private val preferences: SharedPreferenceWrapper) {

    fun resetFirebaseToken() {
        service.resetFirebaseToken(preferences.baseUrl + RESET_FIREBASE_TOKEN_ENDPOINT, MessagingRequest(preferences.firebaseDeviceToken))
            .doOnComplete { preferences.resetFirebase = false }.subscribe()
    }

    companion object {
        private const val RESET_FIREBASE_TOKEN_ENDPOINT = "app/agents/firebase"
    }
}