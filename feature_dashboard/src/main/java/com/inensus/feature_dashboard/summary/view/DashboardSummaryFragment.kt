package com.inensus.feature_dashboard.summary.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inensus.feature_dashboard.R
import com.inensus.feature_dashboard.summary.model.DashboardSummaryData
import com.inensus.feature_dashboard.summary.viewmodel.DashboardSummaryViewModel
import kotlinx.android.synthetic.main.fragment_dashboard_summary.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardSummaryFragment : Fragment() {
    private val viewModel: DashboardSummaryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_dashboard_summary, container, false)

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
        dashboardSummaryBalance.bindData(summary.balance)
        dashboardSummaryProfit.bindData(summary.profit)
        dashboardSummaryDebt.bindData(summary.debt)
        dashboardSummaryAverage.bindData(summary.average, summary.since)
    }
}
