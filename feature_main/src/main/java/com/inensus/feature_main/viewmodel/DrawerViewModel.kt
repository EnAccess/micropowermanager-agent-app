package com.inensus.feature_main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_main.view.DrawerUiState
import com.inensus.feature_main.view.NavigationAction
import com.inensus.shared_agent.service.AgentRepository
import io.reactivex.android.schedulers.AndroidSchedulers

class DrawerViewModel(private val repository: AgentRepository) : BaseViewModel() {

    private val _uiState = MutableLiveData<DrawerUiState>()
    val uiState: LiveData<DrawerUiState> = _uiState

    private val _currentNavigation = LiveEvent<NavigationAction>()
    val currentNavigation: LiveEvent<NavigationAction> = _currentNavigation

    private val _navigationUiState = MutableLiveData<NavigationAction>()
    val navigationUiState: LiveData<NavigationAction> = _navigationUiState

    init {
        getMe()
    }

    fun getMe() {
        _uiState.value = DrawerUiState.AgentLoading

        repository.getMe()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                _uiState.value = DrawerUiState.AgentLoaded(it.email, it.balance)
            }, {
                _uiState.value = DrawerUiState.AgentError
            })
            .addTo(compositeDisposable)
    }

    fun onActionItemTapped(action: NavigationAction) {
        _currentNavigation.value = action
        _navigationUiState.value = action
    }
}