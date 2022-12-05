package com.inensus.feature_login.view

sealed class LoginUiState {
    object Success : LoginUiState()
    object ServerUrl : LoginUiState()

    data class ValidationError(val errors: List<Error>) : LoginUiState() {
        sealed class Error {
            object EmailIsBlank : Error()
            object EmailIsNotInCorrectFormat : Error()
            object PasswordIsBlank : Error()
        }
    }
}