package com.inensus.core_ui.key_value

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.inensus.core_ui.R
import kotlinx.android.synthetic.main.item_key_value_amount.view.*
import kotlinx.android.synthetic.main.item_key_value_default.view.*
import kotlinx.android.synthetic.main.item_key_value_default.view.keyTextView

class KeyValueAdapter(val keyValuePairs: List<KeyValue>) : RecyclerView.Adapter<KeyValueViewHolder<KeyValue>>() {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            KeyValueType.DEFAULT.value -> DefaultViewHolder(parent)
            KeyValueType.AMOUNT.value -> AmountViewHolder(parent)
            else -> DefaultViewHolder(parent)
        } as KeyValueViewHolder<KeyValue>

    override fun getItemViewType(position: Int): Int {
        return keyValuePairs[position].type.value
    }

    override fun onBindViewHolder(holder: KeyValueViewHolder<KeyValue>, position: Int) {
        holder.bind(keyValuePairs[position])
    }

    override fun getItemCount() = keyValuePairs.size

    inner class DefaultViewHolder(itemView: View) : KeyValueViewHolder<KeyValue.Default>(itemView) {
        constructor(parent: ViewGroup)
                : this(LayoutInflater.from(parent.context).inflate(R.layout.item_key_value_default, parent, false))

        override fun bind(item: KeyValue.Default) {
            itemView.apply {
                keyTextView.text = item.key
                valueTextView.text = item.value

                item.textColorId?.let {
                    valueTextView.setTextColor(ContextCompat.getColor(context, it))
                }
            }
        }
    }

    inner class AmountViewHolder(itemView: View) : KeyValueViewHolder<KeyValue.Amount>(itemView) {
        constructor(parent: ViewGroup)
                : this(LayoutInflater.from(parent.context).inflate(R.layout.item_key_value_amount, parent, false))

        override fun bind(item: KeyValue.Amount) {
            itemView.apply {
                titleText.text = item.key
                amountText.text = item.amount
                currencyText.text = item.currency
            }
        }
    }
}