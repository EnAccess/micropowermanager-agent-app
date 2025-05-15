package com.inensus.feature_payment.payment_detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.inensus.core_ui.BaseFragment
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.core_ui.key_value.KeyValueAdapter
import com.inensus.feature_payment.R
import com.inensus.feature_payment.payment_detail.viewmodel.PaymentDetailViewModel
import kotlinx.android.synthetic.main.fragment_payment_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentDetailFragment : BaseFragment() {
    private val viewModel: PaymentDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.fragment_payment_detail, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getLong(EXTRA_PAYMENT_ID)?.let {
            viewModel.getPaymentDetail(it)
        }

        observePaymentDetails()
    }

    override fun provideViewModel() = viewModel

    private fun observePaymentDetails() {
        viewModel.paymentDetails.observe(
            viewLifecycleOwner,
            Observer {
                setupPaymentDetails(it)
            },
        )
    }

    private fun setupPaymentDetails(paymentDetails: List<KeyValue>) {
        rvPaymentDetails.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = KeyValueAdapter(paymentDetails)
        }
    }

    companion object {
        const val EXTRA_PAYMENT_ID = "paymentId"
    }
}
