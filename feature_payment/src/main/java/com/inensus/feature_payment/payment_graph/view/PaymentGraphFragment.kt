package com.inensus.feature_payment.payment_graph.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.inensus.core_ui.BaseFragment
import com.inensus.core_ui.graph.GraphMarkerView
import com.inensus.feature_payment.R
import com.inensus.feature_payment.databinding.FragmentPaymentGraphBinding
import com.inensus.feature_payment.payment_graph.viewmodel.PaymentGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentGraphFragment : BaseFragment() {
    private var _binding: FragmentPaymentGraphBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PaymentGraphViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPaymentGraphBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListeners()
        observeUiState()
    }

    private fun setupView() {
        setupChart()
    }

    private fun setupListeners() {
        binding.periodDropdown.onValueChanged = { viewModel.onPeriodChanged(it) }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner, {
            when (it) {
                is PaymentGraphUiState.Success -> handleSuccessState(it.periodList, it.xAxisList, it.barData)
            }
        })

        viewModel.period.observe(viewLifecycleOwner, {
            if (binding.periodDropdown.value != it.value) {
                binding.periodDropdown.value = it.value
            }
        })
    }

    private fun handleSuccessState(
        periodList: List<String>,
        xAxisList: ArrayList<String>,
        barData: List<List<BarEntry>>,
    ) {
        binding.periodDropdown.bindData(periodList)

        binding.chart.xAxis.valueFormatter = PaymentGraphXAxisFormatter(xAxisList)
        setupChartData(barData)
    }

    private fun setupChart() {
        binding.chart.apply {
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
                typeface = ResourcesCompat.getFont(binding.chart.context, R.font.regular)
                typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
                textColor = ContextCompat.getColor(context, R.color.gray_BDBDBD)
                textSize = 12f
                setDrawAxisLine(false)
                axisMinimum = 0f
            }

            axisRight.isEnabled = false
            isScaleYEnabled = false

            marker = GraphMarkerView(binding.chart.context)

            setExtraOffsets(
                GRAPH_OFFSET,
                GRAPH_OFFSET,
                GRAPH_OFFSET,
                GRAPH_OFFSET,
            )

            setNoDataTextTypeface(ResourcesCompat.getFont(context, R.font.semi_bold))
            setNoDataTextColor(ContextCompat.getColor(context, R.color.gray_BDBDBD))
            setNoDataText(getString(R.string.dashboard_graph_empty_data))
        }
    }

    private fun setupChartData(barData: List<List<BarEntry>>) {
        val groupCount: Float

        val dataSets = ArrayList<IBarDataSet>()

        barData.forEachIndexed { index, list ->
            if (list.isNotEmpty()) {
                dataSets.add(generateDataSet(index, list))
            }
        }

        if (dataSets.isEmpty()) {
            binding.chart.apply {
                data = null
                invalidate()
            }
        } else {
            groupCount = dataSets.maxOf { it.entryCount }.toFloat()

            binding.chart.apply {
                data = BarData(dataSets)
                highlightValue(barData.first().first().x, 0)

                data.notifyDataChanged()
                notifyDataSetChanged()
                invalidate()

                setHardwareAccelerationEnabled(false)

                data.barWidth = GRAPH_BAR_WIDTH
                xAxis.axisMinimum = 0f
                xAxis.axisMaximum = binding.chart.barData.getGroupWidth(GRAPH_GROUP_SPACE, GRAPH_BAR_SPACE) * groupCount
                groupBars(0f, GRAPH_GROUP_SPACE, GRAPH_BAR_SPACE)
                invalidate()
            }
        }
    }

    private fun generateDataSet(
        index: Int,
        entries: List<BarEntry>,
    ) = BarDataSet(
        entries,
        when (index) {
            0 -> getString(R.string.payment_graph_energy)
            1 -> getString(R.string.payment_graph_access_rate)
            2 -> getString(R.string.payment_graph_deferred_payment)
            else -> ""
        },
    ).apply {
        setDrawValues(false)
        setDrawIcons(false)

        color =
            when (index) {
                0 -> ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                1 -> ContextCompat.getColor(requireContext(), R.color.gray_616161)
                2 -> ContextCompat.getColor(requireContext(), R.color.blue_1A237E)
                else -> ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            }
    }

    override fun provideViewModel() = viewModel

    companion object {
        private const val GRAPH_GRANULARITY = 1f
        private const val GRAPH_OFFSET = 16f
        private const val GRAPH_GROUP_SPACE = 0.08f
        private const val GRAPH_BAR_WIDTH = 0.2f
        private const val GRAPH_BAR_SPACE = 0.03f
    }
}
