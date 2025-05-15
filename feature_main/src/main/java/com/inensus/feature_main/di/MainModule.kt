package com.inensus.feature_main.di

import com.inensus.feature_appliance.appliance_detail.di.ApplianceDetailModule
import com.inensus.feature_appliance.appliance_form.di.ApplianceModule
import com.inensus.feature_appliance.appliance_list.di.ApplianceListModule
import com.inensus.feature_customers.customer_list.di.CustomersModule
import com.inensus.feature_dashboard.main.di.DashboardMainModule
import com.inensus.feature_main.viewmodel.DrawerViewModel
import com.inensus.feature_payment.payment_detail.di.PaymentDetailModule
import com.inensus.feature_payment.payment_form.di.PaymentModule
import com.inensus.feature_payment.payment_graph.di.PaymentGraphModule
import com.inensus.feature_payment.payment_list.di.PaymentListModule
import com.inensus.feature_ticket.ticket_detail.di.TicketDetailModule
import com.inensus.feature_ticket.ticket_form.di.TicketFormModule
import com.inensus.feature_ticket.ticket_list.di.TicketListModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object MainModule {
    fun createMainModules(): List<Module> =
        listOf(
            module { viewModel { DrawerViewModel(get()) } },
            *DashboardMainModule.createDashboardModules().toTypedArray(),
            *CustomersModule.createCustomersModules().toTypedArray(),
            *PaymentListModule.createPaymentListModules().toTypedArray(),
            PaymentDetailModule.createPaymentDetailModule(),
            *PaymentModule.createPaymentModules().toTypedArray(),
            PaymentGraphModule.createPaymentGraphModule(),
            *ApplianceListModule.createApplianceListModules().toTypedArray(),
            *ApplianceModule.createApplianceModules().toTypedArray(),
            ApplianceDetailModule.createApplianceDetailModule(),
            *TicketFormModule.createTicketModules().toTypedArray(),
            *TicketListModule.createTicketListModules().toTypedArray(),
            TicketDetailModule.createTicketDetailModule(),
        )
}
