package com.inensus.feature_appliance.appliance_detail.di

import com.inensus.feature_appliance.appliance_detail.view.ApplianceDetailCreator
import com.inensus.feature_appliance.appliance_detail.viewmodel.ApplianceDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ApplianceDetailModule {
    fun createApplianceDetailModule() = module {
        viewModel { ApplianceDetailViewModel(get()) }
        single { ApplianceDetailCreator(get()) }
    }
}