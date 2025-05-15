package com.inensus.feature_dashboard.graph.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.inensus.feature_dashboard.R
import com.inensus.feature_dashboard.databinding.FragmentDashboardGraphMainBinding
import com.inensus.feature_dashboard.graph.view.balance.DashboardGraphBalanceFragment
import com.inensus.feature_dashboard.graph.view.revenue.DashboardGraphRevenueFragment

class DashboardGraphMainFragment : Fragment() {
    private var _binding: FragmentDashboardGraphMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDashboardGraphMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupPagerAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.pager.adapter = null
    }

    private fun setupPagerAdapter() {
        binding.pager.adapter =
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
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position -> tab.text = tabs[position] }.attach()
    }
}
