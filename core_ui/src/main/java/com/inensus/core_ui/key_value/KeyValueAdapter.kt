package com.inensus.core_ui.key_value

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.inensus.core_ui.databinding.ItemKeyValueAmountBinding
import com.inensus.core_ui.databinding.ItemKeyValueDefaultBinding

class KeyValueAdapter(
    val keyValuePairs: List<KeyValue>,
) : RecyclerView.Adapter<KeyValueViewHolder<KeyValue>>() {
    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = when (viewType) {
        KeyValueType.DEFAULT.value ->
            DefaultViewHolder(
                ItemKeyValueDefaultBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )
        KeyValueType.AMOUNT.value ->
            AmountViewHolder(
                ItemKeyValueAmountBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )
        else ->
            DefaultViewHolder(
                ItemKeyValueDefaultBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )
    } as KeyValueViewHolder<KeyValue>

    override fun getItemViewType(position: Int): Int = keyValuePairs[position].type.value

    override fun onBindViewHolder(
        holder: KeyValueViewHolder<KeyValue>,
        position: Int,
    ) {
        holder.bind(keyValuePairs[position])
    }

    override fun getItemCount() = keyValuePairs.size

    inner class DefaultViewHolder(
        private val binding: ItemKeyValueDefaultBinding,
    ) : KeyValueViewHolder<KeyValue.Default>(binding.root) {
        override fun bind(item: KeyValue.Default) {
            with(binding) {
                keyTextView.text = item.key
                valueTextView.text = item.value

                item.textColorId?.let {
                    valueTextView.setTextColor(ContextCompat.getColor(root.context, it))
                }
            }
        }
    }

    inner class AmountViewHolder(
        private val binding: ItemKeyValueAmountBinding,
    ) : KeyValueViewHolder<KeyValue.Amount>(binding.root) {
        override fun bind(item: KeyValue.Amount) {
            with(binding) {
                titleText.text = item.key
                amountText.text = item.amount
                currencyText.text = item.currency
            }
        }
    }
}
