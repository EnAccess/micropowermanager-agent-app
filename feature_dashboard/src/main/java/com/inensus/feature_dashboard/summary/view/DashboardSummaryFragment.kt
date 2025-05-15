package com.inensus.feature_dashboard.summary.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inensus.feature_dashboard.databinding.FragmentDashboardSummaryBinding
import com.inensus.feature_dashboard.summary.model.DashboardSummaryData
import com.inensus.feature_dashboard.summary.viewmodel.DashboardSummaryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardSummaryFragment : Fragment() {
    private var _binding: FragmentDashboardSummaryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardSummaryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDashboardSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        observeUiState()
    }

    private fun observeUiState() {
        viewModel.summaryData.observe(viewLifecycleOwner, {
            handleSummaryData(it)
        })
    }

    private fun handleSummaryData(summary: DashboardSummaryData) {
        binding.dashboardSummaryBalance.bindData(summary.balance)
        binding.dashboardSummaryProfit.bindData(summary.profit)
        binding.dashboardSummaryDebt.bindData(summary.debt)
        binding.dashboardSummaryAverage.bindData(summary.average, summary.since)
    }
}
