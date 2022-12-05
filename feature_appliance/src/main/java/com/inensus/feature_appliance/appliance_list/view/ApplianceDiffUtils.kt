package com.inensus.feature_appliance.appliance_list.view

import androidx.recyclerview.widget.DiffUtil
import com.inensus.core_network.model.ApplianceTransaction

class ApplianceDiffUtils(private val newItems: List<ApplianceTransaction>, private val oldItems: List<ApplianceTransaction>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldItems[oldItemPosition] === newItems[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldItems[oldItemPosition] === newItems[newItemPosition]
}