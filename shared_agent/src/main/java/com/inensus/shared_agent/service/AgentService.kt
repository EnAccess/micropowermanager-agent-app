package com.inensus.shared_agent.service

import com.inensus.shared_agent.model.Agent
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface AgentService {
    @GET
    fun getMe(
        @Url url: String,
    ): Single<Agent>
}
