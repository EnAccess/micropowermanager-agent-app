package com.inensus.core_network.di

import okhttp3.Interceptor

class InterceptorsModel(
    val interceptors: List<Interceptor>,
    val networkInterceptors: List<Interceptor>
)