package com.inensus.feature_customers.customer_list.view

import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
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
import com.inensus.core_ui.extentions.*
import com.inensus.core_ui.util.KeyboardUtils
import com.inensus.feature_customers.R
import com.inensus.feature_customers.customer_list.viewmodel.CustomersViewModel
import kotlinx.android.synthetic.main.fragment_customers.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomerListFragment : Fragment() {
    private val viewModel: CustomersViewModel by viewModel()
    private var searchTerm: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_customers, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeUiState()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.saveCustomersState((rvCustomers.adapter as CustomerListAdapter).customers)
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

        rvCustomers.apply {
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
                val adapter = (rvCustomers.adapter as CustomerListAdapter)

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
            progressBar.show()
        } else {
            rvCustomers.gone()
            loadingLayout.show()
        }
        errorLayout.gone()
        emptyLayout.gone()
    }

    private fun handleError() {
        errorLayout.animateShow()
        loadingLayout.animateGone()
        emptyLayout.gone()
        progressBar.gone()
        rvCustomers.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleEmpty() {
        emptyLayout.animateShow()
        loadingLayout.animateGone()
        errorLayout.gone()
        progressBar.gone()
        (rvCustomers.adapter as CustomerListAdapter).customers = emptyList()
        rvCustomers.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleNoMore() {
        progressBar.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleSuccess(
        customers: List<Customer>,
        loadCustomerType: LoadCustomerType,
    ) {
        rvCustomers.animateShow()
        updateCustomersData(customers, loadCustomerType)
        loadingLayout.animateGone()
        progressBar.gone()
    }

    private fun updateCustomersData(
        customers: List<Customer>,
        loadCustomerType: LoadCustomerType,
    ) {
        val adapter = (rvCustomers.adapter as CustomerListAdapter)

        if (loadCustomerType == LoadCustomerType.PAGINATE) {
            adapter.updateData(ArrayList(adapter.customers).apply { addAll(customers) })
        } else {
            rvCustomers.adapter =
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
