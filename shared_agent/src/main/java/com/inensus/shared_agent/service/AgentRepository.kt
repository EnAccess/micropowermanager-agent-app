package com.inensus.shared_agent.service

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper

class AgentRepository(private val service: AgentService, private val preferences: SharedPreferenceWrapper) {
    fun getMe() = service.getMe(preferences.baseUrl + GET_ME_ENDPOINT)

    companion object {
        private const val GET_ME_ENDPOINT = "app/me"
    }
}