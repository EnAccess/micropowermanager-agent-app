package com.inensus.core_ui.key_value

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class KeyValueViewHolder<in T : KeyValue>(
    view: View,
) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}
