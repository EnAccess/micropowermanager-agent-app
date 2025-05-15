package com.inensus.feature_login.view

import com.inensus.core.Constants.EMAIL_REGEX

class LoginFormValidator {
    fun validateForm(
        username: String?,
        password: String?,
    ): List<LoginUiState.ValidationError.Error> =
        mutableListOf<LoginUiState.ValidationError.Error>().apply {
            if (username.isNullOrEmpty()) {
                add(LoginUiState.ValidationError.Error.EmailIsBlank)
            }

            if (username?.matches(EMAIL_REGEX) == false) {
                add(LoginUiState.ValidationError.Error.EmailIsBlank)
            }

            if (password.isNullOrEmpty()) {
                add(LoginUiState.ValidationError.Error.PasswordIsBlank)
            }
        }
}
