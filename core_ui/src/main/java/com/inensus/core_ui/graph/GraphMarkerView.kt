package com.inensus.core_ui.graph

import android.content.Context
import android.view.LayoutInflater
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.inensus.core.utils.AmountUtils
import com.inensus.core_ui.R
import com.inensus.core_ui.databinding.ViewGraphMarkerBinding
import java.math.BigDecimal

class GraphMarkerView(
    context: Context,
) : MarkerView(context, R.layout.view_graph_marker) {
    private lateinit var binding: ViewGraphMarkerBinding

    init {
        binding = ViewGraphMarkerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun refreshContent(
        entry: Entry,
        highlight: Highlight,
    ) {
        setMarkerText(entry)

        super.refreshContent(entry, highlight)

        setMarkerOffset()
    }

    private fun setMarkerText(entry: Entry) {
        binding.valueTextWhole.text = AmountUtils.convertAmountToString(BigDecimal(entry.y.toDouble()))
    }

    private fun setMarkerOffset() {
        setOffset(-(width / 2).toFloat(), -(height - binding.markerImage.height / 2).toFloat())
    }
}
