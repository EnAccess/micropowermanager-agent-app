package com.inensus.feature_appliance.appliance_form.di

import com.inensus.core_network_auth.AuthQualifiers
import com.inensus.feature_appliance.ApplianceService
import com.inensus.feature_appliance.appliance_form.repository.ApplianceFormRepository
import com.inensus.feature_appliance.appliance_form.view.ApplianceFormValidator
import com.inensus.feature_appliance.appliance_form.view.ApplianceSummaryCreator
import com.inensus.feature_appliance.appliance_form.viewmodel.ApplianceFormViewModel
import com.inensus.feature_appliance.appliance_form.viewmodel.ApplianceSummaryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

object ApplianceModule {
    fun createApplianceModules(): List<Module> =
        listOf(
            module {
                viewModel { ApplianceFormViewModel(get(), get(), get()) }
                viewModel { ApplianceSummaryViewModel(get()) }
                single { ApplianceSummaryCreator(get()) }
                single { ApplianceFormValidator() }
            },
            createApplianceNetworkModule(),
        )

    private fun createApplianceNetworkModule() =
        module {
            single { provideApplianceService(get(qualifier = AuthQualifiers.AUTH_RETROFIT)) }

            single { ApplianceFormRepository(get(), get(), get()) }
        }

    private fun provideApplianceService(retrofitClient: Retrofit) = retrofitClient.create(ApplianceService::class.java)
}
