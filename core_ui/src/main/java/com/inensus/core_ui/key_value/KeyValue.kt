package com.inensus.core_ui.key_value

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class KeyValue(
    val type: KeyValueType,
) : Parcelable {
    @Parcelize
    data class Default(
        val key: String,
        val value: String,
        val textColorId: Int? = null,
    ) : KeyValue(KeyValueType.DEFAULT)

    @Parcelize
    data class Amount(
        val key: String,
        val amount: String,
        val currency: String,
    ) : KeyValue(KeyValueType.AMOUNT)
}

enum class KeyValueType(
    val value: Int,
) {
    DEFAULT(0),
    AMOUNT(1),
}
