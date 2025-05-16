package com.inensus.feature_ticket.ticket_form.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.inensus.core_ui.BaseActivity
import com.inensus.core_ui.BaseFragment
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.core_ui.key_value.KeyValueAdapter
import com.inensus.feature_ticket.R
import com.inensus.feature_ticket.databinding.FragmentTicketSummaryBinding
import com.inensus.feature_ticket.ticket_form.viewmodel.TicketSummaryViewModel
import com.inensus.shared_success.view.SuccessFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class TicketSummaryFragment : BaseFragment() {
    private val viewModel: TicketSummaryViewModel by viewModel()
    private var successFragment: SuccessFragment? = null

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentTicketSummaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTicketSummaryBinding.inflate(inflater, container, false)
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

        arguments?.getParcelableArrayList<KeyValue>(EXTRA_KEY_VALUE_LIST)?.apply {
            setupRecyclerView(this)
        }

        setupListeners()
        observeUiState()
        setupSuccessFragment()
    }

    override fun provideViewModel() = viewModel

    private fun setupRecyclerView(list: List<KeyValue>) {
        binding.rvSummary.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = KeyValueAdapter(list)
        }
    }

    private fun setupListeners() {
        binding.buttonConfirm.setOnClickListener {
            viewModel.onConfirmButtonTapped()
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    TicketSummaryUiState.OpenSuccess -> handleSuccess()
                }
            },
        )
    }

    private fun setupSuccessFragment() {
        (childFragmentManager.findFragmentByTag(SUCCESS_FRAGMENT_TAG) as? SuccessFragment)?.apply {
            if (isAdded) {
                setSuccessCallback(fragment = this)
            }
        }
    }

    private fun handleSuccess() {
        successFragment =
            SuccessFragment.newInstance((binding.rvSummary.adapter as KeyValueAdapter).keyValuePairs).also {
                setSuccessCallback(it)
                it.show(childFragmentManager, SUCCESS_FRAGMENT_TAG)
            }
    }

    private fun setSuccessCallback(fragment: SuccessFragment) {
        fragment.dismissCallback = {
            (activity as BaseActivity).provideNavController().popBackStack(R.id.customersFragment, false)
        }
    }

    companion object {
        const val EXTRA_KEY_VALUE_LIST = "keyValueList"
        const val SUCCESS_FRAGMENT_TAG = "successFragmentTag"
    }
}
