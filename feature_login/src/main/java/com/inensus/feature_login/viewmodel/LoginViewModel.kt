package com.inensus.feature_login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core_network.toServiceError
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_login.service.LoginRepository
import com.inensus.feature_login.view.LoginFormValidator
import com.inensus.feature_login.view.LoginUiState
import io.reactivex.android.schedulers.AndroidSchedulers

class LoginViewModel(
    private val repository: LoginRepository,
    private val validator: LoginFormValidator,
    private val preferences: SharedPreferenceWrapper,
) : BaseViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _uiState = LiveEvent<LoginUiState>()
    val uiState: LiveData<LoginUiState> = _uiState

    init {
        if (preferences.baseUrl.isNullOrEmpty()) {
            _uiState.postValue(LoginUiState.ServerUrl)
        }
    }

    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun onLoginButtonTapped() {
        if (preferences.baseUrl.isNullOrEmpty()) {
            _uiState.value = LoginUiState.ServerUrl
        } else {
            validator.validateForm(_email.value, _password.value).let {
                if (it.isEmpty()) {
                    login()
                } else {
                    _uiState.value = LoginUiState.ValidationError(it)
                }
            }
        }
    }

    private fun login() {
        showLoading()

        repository
            .login(_email.value, _password.value)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                _uiState.value = LoginUiState.Success
            }, {
                handleError(it.toServiceError())
            })
            .addTo(compositeDisposable)
    }
}
