package com.inensus.core_ui.bottom_sheet_input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inensus.core.Constants
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core_ui.R
import com.inensus.core_ui.databinding.FragmentInputBottomSheetBinding
import org.koin.android.ext.android.inject

class InputBottomSheetFragment : BottomSheetDialogFragment() {
    private val preferences: SharedPreferenceWrapper by inject()

    override fun getTheme() = R.style.AppTheme_BottomSheet

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentInputBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)
        val themedInflater = inflater.cloneInContext(contextThemeWrapper)

        _binding = FragmentInputBottomSheetBinding.inflate(themedInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputView.getTitleView().text = getString(R.string.title_server_url)
        binding.inputView.setText(preferences.baseUrl ?: "")
        binding.inputView.getMainTextView().hint = getString(R.string.hint_server_url)

        binding.negativeButton.setOnClickListener {
            dismiss()
        }

        binding.positiveButton.setOnClickListener {
            if (binding.inputView.getText().matches(Constants.HTTP_REGEX)) {
                preferences.baseUrl = binding.inputView.getText()
                dismiss()
            } else {
                binding.inputView.showErrorMessage(getString(R.string.server_url_validation))
            }
        }
    }

    companion object {
        fun newInstance() = InputBottomSheetFragment()
    }
}
