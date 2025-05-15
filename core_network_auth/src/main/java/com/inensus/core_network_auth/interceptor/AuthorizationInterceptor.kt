package com.inensus.core_network_auth.interceptor

import android.content.Context
import android.content.Intent
import com.inensus.core.broadcast.SessionExpireBroadcastReceiver.Companion.SESSION_EXPIRE_INTENT_ACTION
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
    private val context: Context,
    private val sharedPreferenceWrapper: SharedPreferenceWrapper,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain
                .request()
                .newBuilder()
                .addHeader(HEADER_AUTHORIZATION, BEARER + sharedPreferenceWrapper.accessToken)
                .addHeader(HEADER_DEVICE_ID, sharedPreferenceWrapper.deviceId)
                .build()

        val response = chain.proceed(request)

        if (response.code == 401) {
            context.sendBroadcast(Intent(SESSION_EXPIRE_INTENT_ACTION))
        }

        return response
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val HEADER_DEVICE_ID = "device-id"
        private const val BEARER = "Bearer "
    }
}
