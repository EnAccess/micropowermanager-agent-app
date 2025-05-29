package com.inensus.feature_appliance.appliance_list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.inensus.core.utils.AmountUtils
import com.inensus.core.utils.DateUtils
import com.inensus.core_network.model.ApplianceTransaction
import com.inensus.feature_appliance.R
import com.inensus.feature_appliance.databinding.ApplianceListItemBinding
import java.math.BigDecimal

class ApplianceListAdapter : RecyclerView.Adapter<ApplianceListAdapter.ViewHolder>() {
    lateinit var onItemClick: ((appliance: ApplianceTransaction) -> Unit)
    var appliances: List<ApplianceTransaction> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = ApplianceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(appliances[position])
    }

    override fun getItemCount() = appliances.size

    fun updateData(newItems: List<ApplianceTransaction>) {
        val diff = DiffUtil.calculateDiff(ApplianceDiffUtils(newItems, appliances))
        diff.dispatchUpdatesTo(this)
        appliances = newItems
    }

    inner class ViewHolder(
        private val binding: ApplianceListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: ApplianceTransaction) {
            with(binding) {
                val context = root.context
                statusImage.setImageDrawable(
                    if (transaction.rates.none { it.remainingAmount > BigDecimal.ZERO }) {
                        ContextCompat.getDrawable(context, R.drawable.ic_success)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.ic_pending)
                    },
                )
                typeText.text = transaction.appliance.type.name
                priceText.text = AmountUtils.convertAmountToString(transaction.cost)
                dateText.text = DateUtils.convertDateToString(transaction.createdAt)
                tenureValueText.text = transaction.tenure.toString()

                cardView.setOnClickListener {
                    onItemClick.invoke(transaction)
                }
            }
        }
    }
}
