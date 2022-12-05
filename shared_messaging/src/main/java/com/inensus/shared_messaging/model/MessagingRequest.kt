package com.inensus.shared_messaging.model

import com.google.gson.annotations.SerializedName

data class MessagingRequest(@SerializedName("fire_base_token") val firebaseToken: String)