package com.inensus.feature_appliance.appliance_form.view

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
import com.inensus.feature_appliance.R
import com.inensus.feature_appliance.appliance_form.viewmodel.ApplianceSummaryViewModel
import com.inensus.shared_success.view.SuccessFragment
import kotlinx.android.synthetic.main.fragment_appliance_summary.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ApplianceSummaryFragment : BaseFragment() {

    private val viewModel: ApplianceSummaryViewModel by viewModel()
    private var successFragment: SuccessFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_appliance_summary, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        rvSummary.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = KeyValueAdapter(list)
        }
    }

    private fun setupListeners() {
        buttonConfirm.setOnClickListener {
            viewModel.onConfirmButtonTapped()
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            when (it) {
                ApplianceSummaryUiState.OpenSuccess -> handleSuccess()
            }
        })
    }

    private fun setupSuccessFragment() {
        (childFragmentManager.findFragmentByTag(SUCCESS_FRAGMENT_TAG) as? SuccessFragment)?.apply {
            if (isAdded) {
                setSuccessCallback(fragment = this)
            }
        }
    }

    private fun handleSuccess() {
        successFragment = SuccessFragment.newInstance((rvSummary.adapter as KeyValueAdapter).keyValuePairs).also {
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
