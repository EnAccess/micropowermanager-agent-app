package com.inensus.feature_ticket.ticket_form.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.inensus.core_ui.BaseActivity
import com.inensus.core_ui.extentions.compareWithoutTime
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_ticket.R
import com.inensus.feature_ticket.ticket_form.viewmodel.TicketFormViewModel
import kotlinx.android.synthetic.main.fragment_ticket_form.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("UNCHECKED_CAST")
class TicketFormFragment : Fragment() {

    private val viewModel: TicketFormViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_ticket_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        observeUiState()
    }

    private fun setListeners() {
        messageInput.afterTextChanged = { viewModel.onMessageChanged(it.toString()) }
        dueDateInput.onDateSelected = { viewModel.onDueDateChanged(it) }
        categoryDropdown.onValueChanged = { viewModel.onCategoryChanged(it) }

        buttonContinue.setOnClickListener {
            viewModel.onContinueButtonTapped()
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is TicketFormUiState.OpenSummary -> openSummaryPage(it.summaryItems)
                is TicketFormUiState.ValidationError -> handleValidationError(it.errors)
            }
        })

        viewModel.categories.observe(viewLifecycleOwner, Observer {
            categoryDropdown.bindData(it.map { category -> category.name }, viewModel.category.value == null)
        })

        viewModel.message.observe(viewLifecycleOwner, Observer {
            if (it != null && messageInput.getMainTextView().text.toString() != it.toString()) {
                messageInput.getMainTextView().setText(it.toString())
            }
        })

        viewModel.dueDate.observe(viewLifecycleOwner, Observer {
            if (dueDateInput.date?.compareWithoutTime(it) != 0) {
                dueDateInput.date = it
            }
        })

        viewModel.category.observe(viewLifecycleOwner, Observer {
            if (categoryDropdown.value != it.name) {
                categoryDropdown.value = it.name
            }
        })
    }

    private fun openSummaryPage(summaryItems: ArrayList<KeyValue.Default>) {
        (activity as BaseActivity).provideNavController()
            .navigate(R.id.openTicketSummary, bundleOf(TicketSummaryFragment.EXTRA_KEY_VALUE_LIST to summaryItems))
    }

    private fun handleValidationError(errors: List<TicketFormUiState.ValidationError.Error>) {
        errors.forEach { validationError ->
            when (validationError) {
                is TicketFormUiState.ValidationError.Error.MessageIsBlank -> {
                    messageInput.setErrorState(true)
                }
                is TicketFormUiState.ValidationError.Error.CategoryIsBlank -> {
                    categoryDropdown.setErrorState(true)
                }
            }
        }
    }
}