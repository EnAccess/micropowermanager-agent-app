package com.inensus.feature_ticket.ticket_list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.inensus.core.utils.DateUtils
import com.inensus.feature_ticket.R
import com.inensus.feature_ticket.databinding.TicketListItemBinding
import com.inensus.feature_ticket.ticket_list.model.Ticket

class TicketListAdapter : RecyclerView.Adapter<TicketListAdapter.ViewHolder>() {
    lateinit var onItemClick: ((ticket: Ticket) -> Unit)
    var tickets: List<Ticket> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = TicketListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(tickets[position])
    }

    override fun getItemCount() = tickets.size

    fun updateData(newItems: List<Ticket>) {
        val diff = DiffUtil.calculateDiff(TicketDiffUtils(newItems, tickets))
        diff.dispatchUpdatesTo(this)
        tickets = newItems
    }

    inner class ViewHolder(
        private val binding: TicketListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ticket: Ticket) {
            with(binding) {
                val context = root.context
                statusImage.setImageDrawable(
                    when (ticket.status) {
                        1 -> ContextCompat.getDrawable(context, R.drawable.ic_status_close)
                        0 -> ContextCompat.getDrawable(context, R.drawable.ic_status_open)
                        else -> ContextCompat.getDrawable(context, R.drawable.ic_status_open)
                    },
                )
                typeText.text = ticket.category?.name
                senderText.text = context.getString(R.string.customer_name_surname, ticket.owner?.name, ticket.owner?.surname)
                dateText.text = DateUtils.convertDateToString(ticket.createdAt)

                cardView.setOnClickListener {
                    onItemClick.invoke(ticket)
                }
            }
        }
    }
}
