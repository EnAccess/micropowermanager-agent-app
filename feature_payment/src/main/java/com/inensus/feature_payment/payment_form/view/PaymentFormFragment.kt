package com.inensus.feature_payment.payment_form.view

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.inensus.core_ui.BaseActivity
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_payment.R
import com.inensus.feature_payment.databinding.FragmentPaymentFormBinding
import com.inensus.feature_payment.payment_form.viewmodel.PaymentFormViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("UNCHECKED_CAST")
class PaymentFormFragment : Fragment() {
    private var _binding: FragmentPaymentFormBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PaymentFormViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPaymentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setListeners()
        observeUiState()
    }

    private fun setupView() {
        binding.amount.getMainTextView().inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun setListeners() {
        binding.deviceDropdown.onValueChanged = { viewModel.onDeviceChanged(it) }
        binding.amount.afterTextChanged = { viewModel.onAmountChanged(it.toString()) }
        binding.buttonContinue.setOnClickListener {
            viewModel.onContinueButtonTapped()
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is PaymentFormUiState.OpenSummary -> openSummaryPage(it.summaryItems)
                    is PaymentFormUiState.ValidationError -> handleValidationError(it.errors)
                }
            },
        )

        viewModel.customer.observe(
            viewLifecycleOwner,
            Observer { customer ->
                customer?.devices?.let { devices ->
                    binding.deviceDropdown.bindData(devices.map { it.deviceSerial })
                }
            },
        )

        viewModel.device.observe(
            viewLifecycleOwner,
            Observer {
                if (binding.deviceDropdown.value != it) {
                    binding.deviceDropdown.value = it
                }
            },
        )

        viewModel.amount.observe(
            viewLifecycleOwner,
            Observer {
                if (binding.amount
                        .getMainTextView()
                        .text
                        .toString() != it
                ) {
                    binding.amount.getMainTextView().setText(it.toString())
                }
            },
        )
    }

    private fun openSummaryPage(summaryItems: ArrayList<KeyValue>) {
        (activity as BaseActivity)
            .provideNavController()
            .navigate(R.id.openPaymentSummary, bundleOf(PaymentSummaryFragment.EXTRA_KEY_VALUE_LIST to summaryItems))
    }

    private fun handleValidationError(errors: List<PaymentFormUiState.ValidationError.Error>) {
        errors.forEach { validationError ->
            when (validationError) {
                is PaymentFormUiState.ValidationError.Error.DeviceIsBlank -> {
                    binding.deviceDropdown.setErrorState(true)
                }
                is PaymentFormUiState.ValidationError.Error.AmountIsBlank -> {
                    binding.amount.setErrorState(true)
                }
            }
        }
    }
}
