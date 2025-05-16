package com.inensus.feature_payment.payment_list.view

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
import com.inensus.core_network.model.Customer
import com.inensus.core_network.model.Payment
import com.inensus.core_ui.BaseActivity
import com.inensus.core_ui.extentions.animateGone
import com.inensus.core_ui.extentions.animateShow
import com.inensus.core_ui.extentions.gone
import com.inensus.core_ui.extentions.hide
import com.inensus.core_ui.extentions.show
import com.inensus.core_ui.util.KeyboardUtils
import com.inensus.feature_payment.R
import com.inensus.feature_payment.databinding.FragmentPaymentListBinding
import com.inensus.feature_payment.payment_detail.view.PaymentDetailFragment
import com.inensus.feature_payment.payment_list.viewmodel.PaymentListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentListFragment : Fragment() {
    private val viewModel: PaymentListViewModel by viewModel()

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentPaymentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPaymentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.savePaymentsState((binding.rvPayments.adapter as PaymentListAdapter).payments)
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
            viewModel.getPayments(type = LoadingPaymentListType.INITIAL)
        }

        binding.createPayment.setOnClickListener {
            (activity as BaseActivity).provideNavController().navigate(R.id.openPayment)
        }

        binding.rvPayments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter =
                PaymentListAdapter().apply {
                    onItemClick = {
                        viewModel.onPaymentTapped(it)
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
                val adapter = (binding.rvPayments.adapter as PaymentListAdapter)

                if (lastItemPosition + LIST_THRESHOLD > adapter.payments.size && adapter.payments.isNotEmpty()) {
                    viewModel.getPayments(type = LoadingPaymentListType.PAGINATE)
                }
            }
        }

    private fun observeUiState() {
        viewModel.uiState.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is PaymentListUiState.Loading -> handleLoading(it.type)
                    PaymentListUiState.Error -> handleError()
                    PaymentListUiState.NoMoreData -> handleNoMore()
                    PaymentListUiState.Empty -> handleEmpty()
                    is PaymentListUiState.Success -> handleSuccess(it.payments, it.type)
                    is PaymentListUiState.PaymentTapped -> handlePaymentTapped(it.paymentId)
                }
            },
        )

        viewModel.payments.observe(
            viewLifecycleOwner,
            Observer {
                if (it.isEmpty()) {
                    viewModel.getPayments(type = LoadingPaymentListType.INITIAL)
                } else {
                    handleSuccess(it, LoadingPaymentListType.INITIAL)
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

    private fun handleLoading(type: LoadingPaymentListType) {
        if (type == LoadingPaymentListType.PAGINATE) {
            binding.progressBar.show()
        } else {
            binding.rvPayments.gone()
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
        binding.rvPayments.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleEmpty() {
        binding.emptyLayout.root.animateShow()
        binding.loadingLayout.root.animateGone()
        binding.errorLayout.root.gone()
        binding.progressBar.hide()
        (binding.rvPayments.adapter as PaymentListAdapter).payments = emptyList()
        binding.rvPayments.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleNoMore() {
        binding.progressBar.hide()
        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleSuccess(
        payments: List<Payment>,
        loadCustomerType: LoadingPaymentListType,
    ) {
        if (payments != (binding.rvPayments.adapter as PaymentListAdapter).payments) {
            binding.progressBar.hide()
            binding.rvPayments.animateShow()
            updatePaymentsData(payments, loadCustomerType)
            binding.loadingLayout.root.animateGone()
        }
    }

    private fun updatePaymentsData(
        payments: List<Payment>,
        loadCustomerType: LoadingPaymentListType,
    ) {
        val adapter = (binding.rvPayments.adapter as PaymentListAdapter)

        if (loadCustomerType == LoadingPaymentListType.PAGINATE) {
            adapter.updateData(ArrayList(adapter.payments).apply { addAll(payments) })
        } else {
            binding.rvPayments.adapter =
                PaymentListAdapter().apply {
                    onItemClick = {
                        viewModel.onPaymentTapped(it)
                    }

                    this.payments = payments
                    notifyDataSetChanged()
                }
        }
    }

    private fun handlePaymentTapped(paymentId: Long) {
        (activity as BaseActivity).provideNavController().navigate(
            R.id.openPaymentDetail,
            bundleOf(PaymentDetailFragment.EXTRA_PAYMENT_ID to paymentId),
        )
    }

    private fun updateView(customer: Customer?) {
        binding.createPayment.visibility = if (customer != null) View.VISIBLE else View.GONE
        view?.findViewById<TextView>(R.id.tvEmptyDescription)?.text =
            if (customer != null) {
                getString(
                    R.string.payment_empty_layout__with_customer_content,
                    customer.name,
                )
            } else {
                getString(R.string.payment_empty_layout_content)
            }
    }

    companion object {
        private const val LIST_THRESHOLD = 2
    }
}
