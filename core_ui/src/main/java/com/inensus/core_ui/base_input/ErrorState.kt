package com.inensus.core_ui.base_input

import com.inensus.core_ui.R

interface ErrorState {
    fun setErrorState(isError: Boolean)

    companion object {
        val STATE_ERROR = intArrayOf(R.attr.state_error)
    }
}
