package com.inensus.shared_success.view

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.print.PrintHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.core_ui.key_value.KeyValueAdapter
import com.inensus.shared_success.R
import com.inensus.shared_success.databinding.FragmentSuccessBinding

class SuccessFragment : BottomSheetDialogFragment() {
    var dismissCallback: (() -> Unit)? = null

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)
        val themedInflater = inflater.cloneInContext(contextThemeWrapper)

        _binding = FragmentSuccessBinding.inflate(themedInflater, container, false)
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

        val isPending = arguments?.getBoolean(EXTRA_IS_PENDING) ?: false

        if (isPending) {
            binding.headerText.text = getString(R.string.pending_header)
            binding.headerText.setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow_e7b53f))

            binding.bodyText.text = getString(R.string.pending_body)

            binding.stateLottie.setAnimation(R.raw.pending)
        }
    }

    private fun setupListeners() {
        binding.returnButton.setOnClickListener {
            dismiss()
        }

//        print().setOnClickListener {
//            print()
//        }
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
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                BottomSheetBehavior.from(bottomSheet).isDraggable = false
            }
        }
    }

    private fun print() {
        PrintHelper(requireActivity())
            .apply {
                scaleMode = PrintHelper.SCALE_MODE_FIT
            }.also { printHelper ->
                val bitmap = takeScreenShot()
                printHelper.printBitmap(getString(R.string.success_print_file_name), bitmap)
            }
    }

    private fun takeScreenShot(): Bitmap {
        val bitmap: Bitmap = Bitmap.createBitmap(binding.root.width, binding.root.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        binding.root.draw(canvas)

        return bitmap
    }

    override fun onDismiss(dialog: DialogInterface) {
        dismissCallback?.invoke()
    }

    companion object {
        private const val EXTRA_INPUT = "extra_input"
        private const val EXTRA_IS_PENDING = "is_pending"

        fun newInstance(
            keyValues: List<KeyValue>,
            isPending: Boolean = false,
        ) = SuccessFragment().apply {
            arguments = bundleOf(EXTRA_INPUT to keyValues, EXTRA_IS_PENDING to isPending)
        }
    }
}
