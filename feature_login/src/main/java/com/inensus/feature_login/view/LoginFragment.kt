package com.inensus.feature_login.view

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.inensus.core_ui.BaseFragment
import com.inensus.core_ui.bottom_sheet_input.InputBottomSheetFragment
import com.inensus.core_ui.util.CustomTypefaceSpan
import com.inensus.feature_login.R
import com.inensus.feature_login.viewmodel.LoginViewModel
import com.inensus.shared_navigation.Feature
import com.inensus.shared_navigation.SharedNavigation
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class LoginFragment : BaseFragment() {

    private val viewModel: LoginViewModel by stateViewModel()
    private val navigation: SharedNavigation by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()
        setupListeners()
    }

    private fun setupView() {
        setHeaderText()
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is LoginUiState.ServerUrl -> {
                    askForServerUrl()
                }

                is LoginUiState.Success -> {
                    navigation.navigateTo(requireActivity(), Feature.Main)
                }

                is LoginUiState.ValidationError -> {
                    handleValidationError(it.errors)
                }
            }
        })

        viewModel.email.observe(viewLifecycleOwner, Observer {
            if (emailInput.getText() != it) {
                emailInput.setText(it)
            }
        })

        viewModel.password.observe(viewLifecycleOwner, Observer {
            if (passwordInput.getText() != it) {
                passwordInput.setText(it)
            }
        })
    }

    private fun setupListeners() {
        emailInput.afterTextChanged = { viewModel.onEmailChanged(it.toString()) }
        passwordInput.afterTextChanged = { viewModel.onPasswordChanged(it.toString()) }
        loginButton.setOnClickListener { viewModel.onLoginButtonTapped() }

        forgotPasswordText.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.warning))
                .setCancelable(false)
                .setMessage(getString(R.string.login_forgot_password_action))
                .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                .show()
        }
    }

    private fun askForServerUrl() {
        InputBottomSheetFragment.newInstance().also {
            it.show(childFragmentManager, BOTTOM_SHEET_FRAGMENT_TAG)
        }
    }

    private fun setHeaderText() {
        headerText.text = createHeaderSpannableString()
    }

    private fun createHeaderSpannableString(): SpannableStringBuilder {
        val regularPart = getString(R.string.login_title_regular)

        val regularTypeface = ResourcesCompat.getFont(requireContext(), R.font.regular)
        val boldTypeface = ResourcesCompat.getFont(requireContext(), R.font.bold)

        return SpannableStringBuilder().apply {
            append(regularPart)
            append(" ")
            append(getString(R.string.login_title_bold))
            setSpan(
                CustomTypefaceSpan(regularTypeface ?: Typeface.DEFAULT),
                0,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                CustomTypefaceSpan(boldTypeface ?: Typeface.DEFAULT),
                regularPart.length,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun handleValidationError(errors: List<LoginUiState.ValidationError.Error>) {
        errors.forEach { validationError ->
            when (validationError) {
                is LoginUiState.ValidationError.Error.EmailIsBlank,
                is LoginUiState.ValidationError.Error.EmailIsNotInCorrectFormat -> {
                    emailInput.setErrorState(true)
                }

                is LoginUiState.ValidationError.Error.PasswordIsBlank -> {
                    passwordInput.setErrorState(true)
                }
            }
        }
    }

    override fun provideViewModel() = viewModel

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_TAG = "BOTTOM_SHEET_FRAGMENT_TAG"
    }
}