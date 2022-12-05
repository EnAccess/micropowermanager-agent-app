package com.inensus.feature_dashboard.graph.view.revenue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.inensus.core_ui.graph.GraphMarkerView
import com.inensus.feature_dashboard.R
import com.inensus.feature_dashboard.graph.viewmodel.DashboardGraphRevenueViewModel
import kotlinx.android.synthetic.main.fragment_dashboard_graph_revenue.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardGraphRevenueFragment : Fragment() {

    private val viewModel: DashboardGraphRevenueViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_dashboard_graph_revenue, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeUiState()
    }

    private fun setupView() {
        setupChart()
    }

    private fun observeUiState() {
        viewModel.graphRevenueData.observe(viewLifecycleOwner, {
            when (it) {
                is DashboardGraphRevenueUiState.Success -> handleSuccessState(it.xAxisList, it.barData)
            }
        })
    }

    private fun handleSuccessState(xAxisList: ArrayList<String>, barData: List<BarEntry>) {
        chart.xAxis.valueFormatter = DashboardGraphRevenueXAxisFormatter(xAxisList)
        setupChartData(barData)
    }

    private fun setupChart() {
        chart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)

            with(xAxis) {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
                textColor = ContextCompat.getColor(context, R.color.gray_BDBDBD)
                textSize = 12f
                axisLineColor = ContextCompat.getColor(context, R.color.gray_EDEEF5)
                granularity = GRAPH_GRANULARITY
                isGranularityEnabled = true
            }

            with(axisLeft) {
                typeface = ResourcesCompat.getFont(chart.context, R.font.regular)
                typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
                textColor = ContextCompat.getColor(context, R.color.gray_BDBDBD)
                textSize = 12f
                setDrawAxisLine(false)
                axisMinimum = 0f
            }

            legend.isEnabled = false

            axisRight.isEnabled = false
            isScaleYEnabled = false

            marker = GraphMarkerView(chart.context)

            setExtraOffsets(
                GRAPH_OFFSET,
                GRAPH_OFFSET,
                GRAPH_OFFSET,
                GRAPH_OFFSET
            )

            setNoDataTextTypeface(ResourcesCompat.getFont(context, R.font.semi_bold))
            setNoDataTextColor(ContextCompat.getColor(context, R.color.gray_BDBDBD))
            setNoDataText(getString(R.string.dashboard_graph_empty_data))
        }
    }

    private fun setupChartData(barData: List<BarEntry>) {
        val dataSet = mutableListOf<IBarDataSet>(generateDataSet(barData))

        if (dataSet.isEmpty()) {
            chart.apply {
                data = null
                invalidate()
            }
        } else {
            chart.apply {
                data = BarData(dataSet)
                highlightValue(barData.first().x, 0)

                data.notifyDataChanged()
                notifyDataSetChanged()
                invalidate()

                setHardwareAccelerationEnabled(false)
            }
        }
    }

    private fun generateDataSet(entries: List<BarEntry>) =
        BarDataSet(entries, "").apply {
            setDrawValues(false)
            setDrawIcons(false)
            color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        }

    companion object {
        private const val GRAPH_GRANULARITY = 1f
        private const val GRAPH_OFFSET = 16f
    }
}