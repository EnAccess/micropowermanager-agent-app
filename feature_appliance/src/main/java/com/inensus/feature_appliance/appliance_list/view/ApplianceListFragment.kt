package com.inensus.feature_appliance.appliance_list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inensus.core_network.model.ApplianceTransaction
import com.inensus.core_network.model.Customer
import com.inensus.core_ui.BaseActivity
import com.inensus.core_ui.extentions.animateGone
import com.inensus.core_ui.extentions.animateShow
import com.inensus.core_ui.extentions.gone
import com.inensus.core_ui.extentions.hide
import com.inensus.core_ui.extentions.show
import com.inensus.core_ui.util.KeyboardUtils
import com.inensus.feature_appliance.R
import com.inensus.feature_appliance.appliance_detail.view.ApplianceDetailFragment
import com.inensus.feature_appliance.appliance_list.viewmodel.ApplianceListViewModel
import com.inensus.feature_appliance.databinding.FragmentApplianceListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ApplianceListFragment : Fragment() {
    private val viewModel: ApplianceListViewModel by viewModel()

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentApplianceListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentApplianceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.saveAppliancesState((binding.rvAppliances.adapter as ApplianceListAdapter).appliances)
        _binding = null
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeUiState()
    }

    private fun setupView() {
        view?.findViewById<Button>(R.id.btnRetry)?.setOnClickListener {
            viewModel.getAppliances(type = LoadingApplianceListType.INITIAL)
        }

        binding.createAppliance.setOnClickListener {
            (activity as BaseActivity).provideNavController().navigate(R.id.openApplianceForm)
        }

        binding.rvAppliances.apply {
            layoutManager = LinearLayoutManager(context)
            adapter =
                ApplianceListAdapter().apply {
                    onItemClick = {
                        viewModel.onApplianceTapped(it)
                    }
                }

            addOnScrollListener(setupPagination())
        }
    }

    private fun setupPagination() =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int,
            ) {
                val lastItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val adapter = (binding.rvAppliances.adapter as ApplianceListAdapter)

                if (lastItemPosition + LIST_THRESHOLD > adapter.appliances.size && adapter.appliances.isNotEmpty()) {
                    viewModel.getAppliances(type = LoadingApplianceListType.PAGINATE)
                }
            }
        }

    private fun observeUiState() {
        viewModel.uiState.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is ApplianceListUiState.Loading -> handleLoading(it.type)
                    ApplianceListUiState.Error -> handleError()
                    ApplianceListUiState.NoMoreData -> handleNoMore()
                    ApplianceListUiState.Empty -> handleEmpty()
                    is ApplianceListUiState.Success -> handleSuccess(it.appliances, it.type)
                    is ApplianceListUiState.ApplianceTapped -> handleApplianceTapped(it.appliance)
                }
            },
        )

        viewModel.appliances.observe(
            viewLifecycleOwner,
            Observer {
                if (it.isEmpty()) {
                    viewModel.getAppliances(type = LoadingApplianceListType.INITIAL)
                } else {
                    handleSuccess(it, LoadingApplianceListType.INITIAL)
                }
            },
        )

        viewModel.customer.observe(
            viewLifecycleOwner,
            Observer {
                updateView(it)
            },
        )
    }

    private fun handleLoading(type: LoadingApplianceListType) {
        if (type == LoadingApplianceListType.PAGINATE) {
            binding.progressBar.show()
        } else {
            binding.rvAppliances.gone()
            binding.loadingLayout.root.show()
        }
        binding.errorLayout.root.gone()
        binding.emptyLayout.root.gone()
    }

    private fun handleError() {
        binding.errorLayout.root.animateShow()
        binding.loadingLayout.root.animateGone()
        binding.emptyLayout.root.gone()
        binding.progressBar.hide()
        binding.rvAppliances.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleEmpty() {
        binding.emptyLayout.root.animateShow()
        binding.loadingLayout.root.animateGone()
        binding.errorLayout.root.gone()
        binding.progressBar.hide()
        (binding.rvAppliances.adapter as ApplianceListAdapter).appliances = emptyList()
        binding.rvAppliances.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleNoMore() {
        binding.progressBar.hide()
        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleSuccess(
        appliances: List<ApplianceTransaction>,
        loadCustomerType: LoadingApplianceListType,
    ) {
        if (appliances != (binding.rvAppliances.adapter as ApplianceListAdapter).appliances) {
            binding.progressBar.hide()
            binding.rvAppliances.animateShow()
            updateAppliancesData(appliances, loadCustomerType)
            binding.loadingLayout.root.animateGone()
        }
    }

    private fun updateAppliancesData(
        appliances: List<ApplianceTransaction>,
        loadCustomerType: LoadingApplianceListType,
    ) {
        // appliances pages will be empty until Device feature updates has done in app.
        //        viewModel.saveAppliancesState(emptyList<ApplianceTransaction>())

        val adapter = (binding.rvAppliances.adapter as ApplianceListAdapter)

        if (loadCustomerType == LoadingApplianceListType.PAGINATE) {
            adapter.updateData(ArrayList(adapter.appliances).apply { addAll(appliances) })
        } else {
            binding.rvAppliances.adapter =
                ApplianceListAdapter().apply {
                    onItemClick = {
                        viewModel.onApplianceTapped(it)
                    }

                    this.appliances = appliances
                    notifyDataSetChanged()
                }
        }

        viewModel.saveAppliancesState((binding.rvAppliances.adapter as ApplianceListAdapter).appliances)
    }

    private fun handleApplianceTapped(appliance: ApplianceTransaction) {
        (activity as BaseActivity).provideNavController().navigate(
            R.id.openApplianceDetail,
            bundleOf(ApplianceDetailFragment.EXTRA_APPLIANCE to appliance),
        )
    }

    private fun updateView(customer: Customer?) {
        binding.createAppliance.visibility = if (customer != null) View.VISIBLE else View.GONE
        view?.findViewById<TextView>(R.id.tvEmptyDescription)?.text =
            getString(R.string.appliance_under_maintenance_layout_content)
     /*       if (customer != null) getString(
                R.string.appliance_empty_layout__with_customer_content,
                customer.name
            ) else getString(R.string.appliance_empty_layout_content)*/
    }

    companion object {
        private const val LIST_THRESHOLD = 2
    }
}
