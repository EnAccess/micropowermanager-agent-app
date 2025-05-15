package com.inensus.feature_main.view

import com.inensus.feature_main.R

sealed class NavigationAction(
    val fragmentId: Int,
) {
    object OpenDashboard : NavigationAction(R.id.dashboardFragment)

    object OpenCustomers : NavigationAction(R.id.customersFragment)

    object OpenPayments : NavigationAction(R.id.paymentsFragment)

    object OpenAppliances : NavigationAction(R.id.appliancesFragment)

    object OpenTickets : NavigationAction(R.id.ticketsFragment)
}
