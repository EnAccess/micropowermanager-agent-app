package com.inensus.feature_payment.payment_list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.inensus.core.utils.AmountUtils
import com.inensus.core.utils.DateUtils
import com.inensus.core_network.model.Payment
import com.inensus.feature_payment.R
import com.inensus.feature_payment.databinding.PaymentListItemBinding

class PaymentListAdapter : RecyclerView.Adapter<PaymentListAdapter.ViewHolder>() {
    lateinit var onItemClick: ((payment: Payment) -> Unit)
    var payments: List<Payment> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = PaymentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(payments[position])
    }

    override fun getItemCount() = payments.size

    fun updateData(newItems: List<Payment>) {
        val diff = DiffUtil.calculateDiff(PaymentDiffUtils(newItems, payments))
        diff.dispatchUpdatesTo(this)
        payments = newItems
    }

    inner class ViewHolder(
        private val binding: PaymentListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(payment: Payment) {
            with(binding) {
                val context = root.context
                statusImage.setImageDrawable(
                    when (payment.originalTransaction?.status) {
                        1 -> ContextCompat.getDrawable(context, R.drawable.ic_success)
                        0 -> ContextCompat.getDrawable(context, R.drawable.ic_pending)
                        -1 -> ContextCompat.getDrawable(context, R.drawable.ic_status_error)
                        else -> ContextCompat.getDrawable(context, R.drawable.ic_success)
                    },
                )
                typeText.text = payment.type?.ifBlank { payment.transaction?.type } ?: payment.transaction?.type
                senderText.text =
                    context.getString(
                        R.string.customer_name_surname,
                        payment.device?.person?.name,
                        payment.device?.person?.surname,
                    )
                messageText.text = payment.message?.ifBlank { payment.transaction?.message } ?: payment.transaction?.message
                amountText.text = AmountUtils.convertAmountToString(payment.amount)
                dateText.text = DateUtils.convertDateToString(payment.createdAt)

                cardView.setOnClickListener {
                    onItemClick.invoke(payment)
                }
            }
        }
    }
}
