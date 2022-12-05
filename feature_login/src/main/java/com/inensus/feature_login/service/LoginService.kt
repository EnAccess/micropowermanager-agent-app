package com.inensus.feature_login.service

import com.inensus.feature_login.model.LoginRequest
import com.inensus.feature_login.model.LoginResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface LoginService {
    @POST
    fun login(@Url url: String, @Header("device-id") deviceId: String, @Body request: LoginRequest): Single<LoginResponse>
}