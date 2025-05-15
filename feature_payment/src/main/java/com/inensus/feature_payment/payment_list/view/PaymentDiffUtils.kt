package com.inensus.feature_payment.payment_list.view

import androidx.recyclerview.widget.DiffUtil
import com.inensus.core_network.model.Payment

class PaymentDiffUtils(
    private val newItems: List<Payment>,
    private val oldItems: List<Payment>,
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
