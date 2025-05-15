package com.inensus.feature_main.view

import androidx.navigation.NavController
import com.inensus.feature_main.R

class MainActivityNavigation(
    private val navController: NavController,
) {
    fun handleNavigationAction(action: NavigationAction) {
        if (navController.currentDestination?.id != action.fragmentId) {
            when (action) {
                NavigationAction.OpenDashboard -> {
                    navController.popBackStack(R.id.dashboardFragment, false)
                }
                NavigationAction.OpenCustomers -> {
                    navController.popBackStack(R.id.dashboardFragment, false)
                    navController.navigate(R.id.customersFragment)
                }
                NavigationAction.OpenPayments -> {
                    navController.popBackStack(R.id.dashboardFragment, false)
                    navController.navigate(R.id.paymentsFragment)
                }
                NavigationAction.OpenAppliances -> {
                    navController.popBackStack(R.id.dashboardFragment, false)
                    navController.navigate(R.id.appliancesFragment)
                }
                NavigationAction.OpenTickets -> {
                    navController.popBackStack(R.id.dashboardFragment, false)
                    navController.navigate(R.id.ticketsFragment)
                }
            }
        }
    }
}
