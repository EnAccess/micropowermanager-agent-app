package com.inensus.feature_appliance.appliance_form.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.inensus.core.utils.AmountUtils
import com.inensus.core_network.model.Appliance
import com.inensus.core_ui.BaseActivity
import com.inensus.core_ui.extentions.compareWithoutTime
import com.inensus.core_ui.extentions.gone
import com.inensus.core_ui.extentions.show
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_appliance.R
import com.inensus.feature_appliance.appliance_form.viewmodel.ApplianceFormViewModel
import com.inensus.feature_appliance.databinding.FragmentApplianceFormBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("UNCHECKED_CAST")
class ApplianceFormFragment : Fragment() {
    private var _binding: FragmentApplianceFormBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ApplianceFormViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentApplianceFormBinding.inflate(inflater, container, false)
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

        viewModel.getAppliances()
    }

    private fun setupView() {
        with(binding.downPayment.getMainTextView()) {
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        with(binding.tenure.getMainTextView()) {
            inputType = InputType.TYPE_CLASS_NUMBER
            filters = arrayOf<InputFilter>(LengthFilter(2))
        }
    }

    private fun setListeners() {
        view?.findViewById<Button>(R.id.btnRetry)?.setOnClickListener { viewModel.getAppliances() }
        binding.applianceDropdown.onValueChanged = { viewModel.onApplianceChanged(it) }
        binding.paymentDate.onDateSelected = { viewModel.onFirstPaymentDateChanged(it) }
        binding.downPayment.afterTextChanged = {
            if (it.toString() == "0") {
                binding.downPayment.setText("")
            } else {
                viewModel.onDownPaymentChanged(it.toString())
            }
        }
        binding.tenure.afterTextChanged = {
            if (it.toString() == "0") {
                binding.tenure.setText("")
            } else {
                viewModel.onTenureChanged(it.toString())
            }
        }
        binding.buttonContinue.setOnClickListener { viewModel.onContinueButtonTapped() }
    }

    @SuppressLint("SetTextI18n")
    private fun observeUiState() {
        viewModel.uiState.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is ApplianceFormUiState.Loading -> handleAppliancesLoadingState()
                    is ApplianceFormUiState.AppliancesLoaded -> handleAppliancesLoadedState(it.appliances)
                    is ApplianceFormUiState.Error -> handleErrorState()
                    is ApplianceFormUiState.OpenSummary -> openSummaryPage(it.summaryItems)
                    is ApplianceFormUiState.ValidationError -> handleValidationError(it.errors)
                }
            },
        )

        viewModel.appliance.observe(
            viewLifecycleOwner,
            Observer {
                if (binding.applianceDropdown.value != it.type.name) {
                    binding.applianceDropdown.value = it.type.name
                }
            },
        )

        viewModel.firstPaymentDate.observe(
            viewLifecycleOwner,
            Observer {
                if (binding.paymentDate.date?.compareWithoutTime(it) != 0) {
                    binding.paymentDate.date = it
                }
            },
        )

        viewModel.downPayment.observe(
            viewLifecycleOwner,
            Observer {
                if (it != null &&
                    binding.downPayment
                        .getMainTextView()
                        .text
                        .toString() != it.toString()
                ) {
                    binding.downPayment.getMainTextView().setText(it.toString())
                }
            },
        )

        viewModel.tenure.observe(
            viewLifecycleOwner,
            Observer {
                if (it != null &&
                    binding.tenure
                        .getMainTextView()
                        .text
                        .toString() != it.toString()
                ) {
                    binding.tenure.getMainTextView().setText(it.toString())
                }
            },
        )

        viewModel.monthlyPaymentAmount.observe(
            viewLifecycleOwner,
            Observer {
                if (binding.amountValue.text != it) {
                    binding.amountValue.text = AmountUtils.convertAmountToString(it)
                }
            },
        )
    }

    private fun handleAppliancesLoadingState() {
        binding.loadingLayout.root.show()
        binding.errorLayout.root.gone()
        binding.inputForm.gone()
    }

    private fun handleAppliancesLoadedState(appliances: List<Appliance>) {
        if (appliances.isEmpty()) {
            binding.errorLayout.root.show()
            view?.findViewById<TextView>(R.id.tvErrorDescription)?.text = getString(R.string.appliance_to_sell_empty)
            binding.inputForm.gone()
            binding.loadingLayout.root.gone()
        } else {
            binding.applianceDropdown.bindData(appliances.map { it.type.name })

            binding.inputForm.show()
            binding.errorLayout.root.gone()
            binding.loadingLayout.root.gone()
        }
    }

    private fun handleErrorState() {
        binding.errorLayout.root.show()
        binding.inputForm.gone()
        binding.loadingLayout.root.gone()
    }

    private fun openSummaryPage(summaryItems: ArrayList<KeyValue>) {
        (activity as BaseActivity)
            .provideNavController()
            .navigate(R.id.openApplianceSummary, bundleOf(ApplianceSummaryFragment.EXTRA_KEY_VALUE_LIST to summaryItems))
    }

    private fun handleValidationError(errors: List<ApplianceFormUiState.ValidationError.Error>) {
        errors.forEach { validationError ->
            when (validationError) {
                is ApplianceFormUiState.ValidationError.Error.ApplianceIsBlank -> {
                    binding.applianceDropdown.setErrorState(true)
                }
                is ApplianceFormUiState.ValidationError.Error.ApplianceAmountIsMissing -> {
                    Toast.makeText(context, getString(R.string.appliance_type_amount_error), Toast.LENGTH_LONG).show()
                    binding.applianceDropdown.setErrorState(true)
                }
                is ApplianceFormUiState.ValidationError.Error.FirstPaymentDateIsBlank -> {
                    binding.paymentDate.setErrorState(true)
                }
                is ApplianceFormUiState.ValidationError.Error.DownPaymentMoreThanAppliance -> {
                    Toast.makeText(context, getString(R.string.appliance_down_payment_error), Toast.LENGTH_LONG).show()
                    binding.downPayment.setErrorState(true)
                }
                is ApplianceFormUiState.ValidationError.Error.TenureIsBlank -> {
                    binding.tenure.setErrorState(true)
                }
            }
        }
    }
}
