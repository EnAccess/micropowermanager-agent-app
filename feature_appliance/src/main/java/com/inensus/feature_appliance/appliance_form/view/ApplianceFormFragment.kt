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
import kotlinx.android.synthetic.main.fragment_appliance_form.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("UNCHECKED_CAST")
class ApplianceFormFragment : Fragment() {

    private val viewModel: ApplianceFormViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_appliance_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setListeners()
        observeUiState()

        viewModel.getAppliances()
    }

    private fun setupView() {
        with(downPayment.getMainTextView()) {
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        with(tenure.getMainTextView()) {
            inputType = InputType.TYPE_CLASS_NUMBER
            filters = arrayOf<InputFilter>(LengthFilter(2))
        }
    }

    private fun setListeners() {
        view?.findViewById<Button>(R.id.btnRetry)?.setOnClickListener { viewModel.getAppliances() }
        applianceDropdown.onValueChanged = { viewModel.onApplianceChanged(it) }
        paymentDate.onDateSelected = { viewModel.onFirstPaymentDateChanged(it) }
        downPayment.afterTextChanged = {
            if (it.toString() == "0") {
                downPayment.setText("")
            } else {
                viewModel.onDownPaymentChanged(it.toString())
            }
        }
        tenure.afterTextChanged = {
            if (it.toString() == "0") {
                tenure.setText("")
            } else {
                viewModel.onTenureChanged(it.toString())
            }
        }
        buttonContinue.setOnClickListener { viewModel.onContinueButtonTapped() }
    }

    @SuppressLint("SetTextI18n")
    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ApplianceFormUiState.Loading -> handleAppliancesLoadingState()
                is ApplianceFormUiState.AppliancesLoaded -> handleAppliancesLoadedState(it.appliances)
                is ApplianceFormUiState.Error -> handleErrorState()
                is ApplianceFormUiState.OpenSummary -> openSummaryPage(it.summaryItems)
                is ApplianceFormUiState.ValidationError -> handleValidationError(it.errors)
            }
        })

        viewModel.appliance.observe(viewLifecycleOwner, Observer {
            if (applianceDropdown.value != it.type.name) {
                applianceDropdown.value = it.type.name
            }
        })

        viewModel.firstPaymentDate.observe(viewLifecycleOwner, Observer {
            if (paymentDate.date?.compareWithoutTime(it) != 0) {
                paymentDate.date = it
            }
        })

        viewModel.downPayment.observe(viewLifecycleOwner, Observer {
            if (it != null && downPayment.getMainTextView().text.toString() != it.toString()) {
                downPayment.getMainTextView().setText(it.toString())
            }
        })

        viewModel.tenure.observe(viewLifecycleOwner, Observer {
            if (it != null && tenure.getMainTextView().text.toString() != it.toString()) {
                tenure.getMainTextView().setText(it.toString())
            }
        })

        viewModel.monthlyPaymentAmount.observe(viewLifecycleOwner, Observer {
            if (amountValue.text != it) {
                amountValue.text = AmountUtils.convertAmountToString(it)
            }
        })
    }

    private fun handleAppliancesLoadingState() {
        loadingLayout.show()
        errorLayout.gone()
        inputForm.gone()
    }

    private fun handleAppliancesLoadedState(appliances: List<Appliance>) {
        if (appliances.isEmpty()) {
            errorLayout.show()
            view?.findViewById<TextView>(R.id.tvErrorDescription)?.text = getString(R.string.appliance_to_sell_empty)
            inputForm.gone()
            loadingLayout.gone()
        } else {
            applianceDropdown.bindData(appliances.map { it.type.name })

            inputForm.show()
            errorLayout.gone()
            loadingLayout.gone()
        }
    }

    private fun handleErrorState() {
        errorLayout.show()
        inputForm.gone()
        loadingLayout.gone()
    }

    private fun openSummaryPage(summaryItems: ArrayList<KeyValue>) {
        (activity as BaseActivity).provideNavController()
            .navigate(R.id.openApplianceSummary, bundleOf(ApplianceSummaryFragment.EXTRA_KEY_VALUE_LIST to summaryItems))
    }

    private fun handleValidationError(errors: List<ApplianceFormUiState.ValidationError.Error>) {
        errors.forEach { validationError ->
            when (validationError) {
                is ApplianceFormUiState.ValidationError.Error.ApplianceIsBlank -> {
                    applianceDropdown.setErrorState(true)
                }
                is ApplianceFormUiState.ValidationError.Error.ApplianceAmountIsMissing -> {
                    Toast.makeText(context, getString(R.string.appliance_type_amount_error), Toast.LENGTH_LONG).show()
                    applianceDropdown.setErrorState(true)
                }
                is ApplianceFormUiState.ValidationError.Error.FirstPaymentDateIsBlank -> {
                    paymentDate.setErrorState(true)
                }
                is ApplianceFormUiState.ValidationError.Error.DownPaymentMoreThanAppliance -> {
                    Toast.makeText(context, getString(R.string.appliance_down_payment_error), Toast.LENGTH_LONG).show()
                    downPayment.setErrorState(true)
                }
                is ApplianceFormUiState.ValidationError.Error.TenureIsBlank -> {
                    tenure.setErrorState(true)
                }
            }
        }
    }
}
