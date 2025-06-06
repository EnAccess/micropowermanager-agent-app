package com.inensus.feature_customers.customer_list.view

import androidx.recyclerview.widget.DiffUtil
import com.inensus.core_network.model.Customer

class CustomerDiffUtils(
    private val newItems: List<Customer>,
    private val oldItems: List<Customer>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ) = oldItems[oldItemPosition] === newItems[newItemPosition]

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ) = oldItems[oldItemPosition] === newItems[newItemPosition]
}
