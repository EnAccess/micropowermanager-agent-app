package com.inensus.feature_appliance.appliance_detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.core_ui.key_value.KeyValueAdapter
import com.inensus.feature_appliance.R
import com.inensus.feature_appliance.databinding.FragmentApplianceDetailPaymentsBinding

class ApplianceDetailPaymentsFragment : BottomSheetDialogFragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentApplianceDetailPaymentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)
        val themedInflater = inflater.cloneInContext(contextThemeWrapper)

        _binding = FragmentApplianceDetailPaymentsBinding.inflate(themedInflater, container, false)
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

        setupView()
        setupListeners()
        setupBottomSheetBehavior()
    }

    override fun getTheme() = R.style.AppTheme_BottomSheet

    private fun setupView() {
        arguments?.getParcelableArrayList<KeyValue>(EXTRA_INPUT)?.apply {
            setupRecyclerView(this)
        }
    }

    private fun setupListeners() {
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun setupRecyclerView(list: List<KeyValue>) {
        binding.rvSuccess.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = KeyValueAdapter(list)
        }
    }

    private fun setupBottomSheetBehavior() {
        dialog?.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(R.id.design_bottom_sheet)

            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED

                behavior.addBottomSheetCallback(
                    object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(
                            bottomSheet: View,
                            newState: Int,
                        ) {}

                        override fun onSlide(
                            bottomSheet: View,
                            slideOffset: Float,
                        ) {
                            if (slideOffset > 0) {
                                binding.closeImageView.rotation = slideOffset * ROTATION_COEFFICIENT
                            }
                        }
                    },
                )
            }
        }
    }

    companion object {
        private const val EXTRA_INPUT = "extra_input"
        private const val ROTATION_COEFFICIENT = 180

        fun newInstance(keyValues: List<KeyValue>) =
            ApplianceDetailPaymentsFragment().apply {
                arguments = bundleOf(EXTRA_INPUT to keyValues)
            }
    }
}
