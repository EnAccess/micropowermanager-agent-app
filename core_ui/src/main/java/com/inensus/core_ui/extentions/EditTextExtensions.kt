package com.inensus.core_ui.extentions

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.inensus.core_ui.util.KeyboardUtils
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

fun EditText.requestFocusWithKeyboard() {
    requestFocus()
    context.let {
        if (it is Activity) {
            KeyboardUtils.showKeyboard(it)
        }
    }
}

fun EditText.afterTextChanged(afterTextChanged: (Editable) -> Unit) {
    this.addTextChangedListener(
        object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int,
            ) {
            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int,
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                editable?.run {
                    afterTextChanged.invoke(this)
                }
            }
        },
    )
}

fun EditText.textChangedWithDelay(
    delay: Long = 800,
    afterTextChanged: (Editable) -> Unit,
): Disposable =
    RxTextView
        .textChanges(this)
        .skipInitialValue()
        .debounce(delay, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            afterTextChanged.invoke(this.text)
        }, Timber::e)
