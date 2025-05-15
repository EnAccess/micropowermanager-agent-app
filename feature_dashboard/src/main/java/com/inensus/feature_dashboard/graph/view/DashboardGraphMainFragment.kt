package com.inensus.feature_dashboard.graph.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.inensus.feature_dashboard.R
import com.inensus.feature_dashboard.graph.view.balance.DashboardGraphBalanceFragment
import com.inensus.feature_dashboard.graph.view.revenue.DashboardGraphRevenueFragment
import kotlinx.android.synthetic.main.fragment_dashboard_graph_main.*

class DashboardGraphMainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_dashboard_graph_main, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupPagerAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        pager.adapter = null
    }

    private fun setupPagerAdapter() {
        pager.adapter =
            DashboardGraphMainViewPagerAdapter(
                listOf(
                    DashboardGraphBalanceFragment(),
                    DashboardGraphRevenueFragment(),
                ),
                childFragmentManager,
                viewLifecycleOwner.lifecycle,
            )
        setupTabs()
    }

    private fun setupTabs() {
        val tabs = resources.getStringArray(R.array.dashboard_graph_main_tabs)
        TabLayoutMediator(tabLayout, pager) { tab, position -> tab.text = tabs[position] }.attach()
    }
}
