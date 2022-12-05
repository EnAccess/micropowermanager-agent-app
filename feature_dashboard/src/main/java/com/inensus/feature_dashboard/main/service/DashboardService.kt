package com.inensus.feature_dashboard.main.service

import com.google.gson.JsonObject
import com.inensus.feature_dashboard.summary.model.DashboardSummaryResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface DashboardService {
    @GET
    fun getDashboardSummary(@Url url: String): Single<DashboardSummaryResponse>

    @GET
    fun getDashboardGraphBalance(@Url url: String): Single<JsonObject>

    @GET
    fun getDashboardGraphRevenue(@Url url: String): Single<JsonObject>
}