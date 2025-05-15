package com.inensus.feature_login.service

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.feature_login.model.LoginRequest

class LoginRepository(
    private val service: LoginService,
    private val preferences: SharedPreferenceWrapper,
) {
    fun login(
        email: String?,
        password: String?,
    ) = service
        .login(preferences.baseUrl + LOGIN_ENDPOINT, preferences.deviceId, LoginRequest(email, password))
        .doOnSuccess { preferences.accessToken = it.accessToken }

    companion object {
        private const val LOGIN_ENDPOINT = "app/login"
    }
}
