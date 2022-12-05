package com.inensus.core_ui.date_input

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.children
import com.inensus.core.utils.DateUtils
import com.inensus.core_ui.R
import com.inensus.core_ui.base_input.ErrorState
import com.inensus.core_ui.extentions.getDate
import com.inensus.core_ui.extentions.hide
import com.inensus.core_ui.extentions.show
import kotlinx.android.synthetic.main.view_dropdown_input.view.*
import java.util.*

class DateInputView(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet), ErrorState {
    var onDateSelected: ((Date?) -> Unit)? = null
    private var errorState: Boolean = false

    var date: Date? = null
        set(value) {
            value?.let {
                field = it
                placeholderText.hide()
                valueText.show()
                valueText.text = DateUtils.convertDateToString(it)
                setErrorState(false)
            } ?: apply {
                field = null
                placeholderText.show()
                valueText.hide()
            }

            onDateSelected?.invoke(value)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_date_input, this, true)
        initializeAttributes(context, attributeSet)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray =
        if (errorState) {
            val drawableState = super.onCreateDrawableState(extraSpace + 1)
            mergeDrawableStates(drawableState, ErrorState.STATE_ERROR)
            drawableState
        } else {
            super.onCreateDrawableState(extraSpace)
        }

    private fun initializeAttributes(context: Context, attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.DateInputView).use {
            val title = it.getString(R.styleable.DateInputView_date_input_title)

            setupView(title)
        }
    }

    private fun setupView(title: String?) {
        titleText.text = title

        setOnClickListener {
            openDatePicker()
        }
    }

    override fun setErrorState(isError: Boolean) {
        errorState = isError

        children
            .filter { it is ErrorState }
            .forEach {
                (it as ErrorState).setErrorState(isError)
            }

        refreshDrawableState()
    }

    private fun openDatePicker() {
        val calendar = date?.let { it ->
            Calendar.getInstance().apply {
                time = it
            }
        }

        context?.let {
            DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { view, _, _, _ ->
                    date = view.getDate()
                },
                calendar?.get(Calendar.YEAR) ?: -1,
                calendar?.get(Calendar.MONTH) ?: -1,
                calendar?.get(Calendar.DAY_OF_MONTH) ?: -1
            ).apply {
                datePicker.minDate = Calendar.getInstance().timeInMillis
                show()
            }
        }
    }
}