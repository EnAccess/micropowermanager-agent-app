package com.inensus.feature_dashboard.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_dashboard.main.di.DashboardMainModule
import com.inensus.feature_dashboard.main.service.DashboardRepository
import com.inensus.feature_dashboard.main.view.DashboardMainUiState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Singles
import org.koin.java.KoinJavaComponent
import timber.log.Timber

class DashboardMainViewModel(private val repository: DashboardRepository) : BaseViewModel() {

    private val _uiState = MutableLiveData<DashboardMainUiState>()
    val uiState: LiveData<DashboardMainUiState> = _uiState

    private val _messagingReceiverState = LiveEvent<Unit>()
    val messagingReceiverState: LiveData<Unit> = _messagingReceiverState

    init {
        initializeMessagingReceiver()
        getDashboardData()
    }

    private fun initializeMessagingReceiver() {
        _messagingReceiverState.postValue(Unit)
    }

    fun getDashboardData() {
        _uiState.value = DashboardMainUiState.Loading

        Singles.zip(
            repository.getDashboardSummary(),
            repository.getDashboardGraphBalance(),
            repository.getDashboardGraphRevenue()
        ) { _, _, _ -> }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _uiState.value = DashboardMainUiState.Success
            }, {
                Timber.e((it))
                _uiState.value = DashboardMainUiState.Error
            })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        KoinJavaComponent.getKoin().getScope(DashboardMainModule.DASHBOARD_SCOPE).close()
    }
}