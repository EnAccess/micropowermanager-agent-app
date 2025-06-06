package com.inensus.feature_customers.customer_list.view

import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.utils.setPaddingStart
import com.inensus.core_network.model.Customer
import com.inensus.core_ui.extentions.animateGone
import com.inensus.core_ui.extentions.animateShow
import com.inensus.core_ui.extentions.gone
import com.inensus.core_ui.extentions.show
import com.inensus.core_ui.extentions.textChangedWithDelay
import com.inensus.core_ui.extentions.toPx
import com.inensus.core_ui.util.KeyboardUtils
import com.inensus.feature_customers.R
import com.inensus.feature_customers.customer_list.viewmodel.CustomersViewModel
import com.inensus.feature_customers.databinding.FragmentCustomersBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomerListFragment : Fragment() {
    private val viewModel: CustomersViewModel by viewModel()
    private var searchTerm: String? = null

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentCustomersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentCustomersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.saveCustomersState((binding.rvCustomers.adapter as CustomerListAdapter).customers)
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

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater,
    ) {
        inflater.inflate(R.menu.menu_customers, menu)

        setupSearchView(menu)
    }

    private fun setupView() {
        view?.findViewById<Button>(R.id.btnRetry)?.setOnClickListener {
            viewModel.getCustomers(type = LoadCustomerType.INITIAL)
        }

        binding.rvCustomers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter =
                CustomerListAdapter().apply {
                    onItemClick = {
                        viewModel.onCustomerTapped(it)
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
                val adapter = (binding.rvCustomers.adapter as CustomerListAdapter)

                if (lastItemPosition + LIST_THRESHOLD > adapter.customers.size && adapter.customers.isNotEmpty()) {
                    viewModel.getCustomers(type = LoadCustomerType.PAGINATE)
                }
            }
        }

    private fun observeUiState() {
        viewModel.uiState.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is CustomerListUiState.Loading -> handleLoading(it.type)
                    CustomerListUiState.Error -> handleError()
                    CustomerListUiState.NoMoreData -> handleNoMore()
                    CustomerListUiState.Empty -> handleEmpty()
                    is CustomerListUiState.Success -> handleSuccess(it.customers, it.type)
                    is CustomerListUiState.CustomerTapped -> handleCustomerTapped()
                }
            },
        )

        viewModel.customers.observe(
            viewLifecycleOwner,
            Observer {
                if (it.isEmpty()) {
                    handleEmpty()
                } else {
                    handleSuccess(it, LoadCustomerType.INITIAL)
                }
            },
        )

        viewModel.searchTerm.observe(
            viewLifecycleOwner,
            Observer {
                searchTerm = it
            },
        )
    }

    private fun handleLoading(type: LoadCustomerType) {
        if (type == LoadCustomerType.PAGINATE) {
            binding.progressBar.show()
        } else {
            binding.rvCustomers.gone()
            binding.loadingLayout.root.show()
        }
        binding.errorLayout.root.gone()
        binding.emptyLayout.root.gone()
    }

    private fun handleError() {
        binding.errorLayout.root.animateShow()
        binding.loadingLayout.root.animateGone()
        binding.emptyLayout.root.gone()
        binding.progressBar.gone()
        binding.rvCustomers.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleEmpty() {
        binding.emptyLayout.root.animateShow()
        binding.loadingLayout.root.animateGone()
        binding.errorLayout.root.gone()
        binding.progressBar.gone()
        (binding.rvCustomers.adapter as CustomerListAdapter).customers = emptyList()
        binding.rvCustomers.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleNoMore() {
        binding.progressBar.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleSuccess(
        customers: List<Customer>,
        loadCustomerType: LoadCustomerType,
    ) {
        binding.rvCustomers.animateShow()
        updateCustomersData(customers, loadCustomerType)
        binding.loadingLayout.root.animateGone()
        binding.progressBar.gone()
    }

    private fun updateCustomersData(
        customers: List<Customer>,
        loadCustomerType: LoadCustomerType,
    ) {
        val adapter = (binding.rvCustomers.adapter as CustomerListAdapter)

        if (loadCustomerType == LoadCustomerType.PAGINATE) {
            adapter.updateData(ArrayList(adapter.customers).apply { addAll(customers) })
        } else {
            binding.rvCustomers.adapter =
                CustomerListAdapter().apply {
                    onItemClick = {
                        viewModel.onCustomerTapped(it)
                    }

                    this.customers = customers
                    notifyDataSetChanged()
                }
        }
    }

    private fun handleCustomerTapped() {
        findNavController().navigate(R.id.openCustomerDetail)
    }

    private fun setupSearchView(menu: Menu) {
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        (menu.findItem(R.id.actionSearch).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setPaddingStart((-15).toPx())

            findViewById<EditText>(R.id.search_src_text).apply {
                typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
                setHintTextColor(ContextCompat.getColor(context, R.color.white))
                setTextColor(ContextCompat.getColor(context, R.color.white))
                textSize = 16f
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setTextCursorDrawable(R.drawable.search_cursor)
                }

                if (searchTerm?.isNotBlank() == true) {
                    onActionViewExpanded()
                    setText(searchTerm)
                    setSelection(text.length)
                }

                textChangedWithDelay {
                    viewModel.getCustomers(it.toString(), LoadCustomerType.SEARCH)
                }
            }

            findViewById<ImageView>(R.id.search_close_btn).apply {
                setImageResource(R.drawable.ic_close)
                setOnClickListener {
                    onActionViewCollapsed()
                }
            }
        }
    }

    companion object {
        private const val LIST_THRESHOLD = 2
    }
}
