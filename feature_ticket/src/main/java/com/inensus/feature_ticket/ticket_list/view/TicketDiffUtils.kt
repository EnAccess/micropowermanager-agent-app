package com.inensus.feature_ticket.ticket_list.view

import androidx.recyclerview.widget.DiffUtil
import com.inensus.feature_ticket.ticket_list.model.Ticket

class TicketDiffUtils(
    private val newItems: List<Ticket>,
    private val oldItems: List<Ticket>,
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
