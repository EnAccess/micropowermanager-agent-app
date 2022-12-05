package com.inensus.core_ui.extentions

import android.graphics.Typeface
import com.amulyakhare.textdrawable.TextDrawable

fun createInitialsDrawable(name: String, textColor: Int, backgroundColor: Int): TextDrawable = TextDrawable.builder()
    .beginConfig()
    .fontSize(14.toPx())
    .useFont(Typeface.DEFAULT)
    .textColor(textColor)
    .endConfig()
    .buildRound(extractUserInitials(name), backgroundColor)

private fun extractUserInitials(text: String): String {
    val sequence = text.split(" ").take(2).filterNot { it.isEmpty() }.map { it.first().toString() }
    return when {
        sequence.isEmpty() -> ""
        sequence.size == 1 -> sequence[0]
        else -> listOf(sequence[0], sequence.last()).joinToString("")
    }
}
