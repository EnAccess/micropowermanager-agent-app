package com.inensus.feature_payment.payment_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.inensus.feature_payment.R
import com.inensus.feature_payment.databinding.FragmentPaymentMainBinding
import com.inensus.feature_payment.payment_graph.view.PaymentGraphFragment
import com.inensus.feature_payment.payment_list.view.PaymentListFragment

class PaymentMainFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentPaymentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPaymentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.pager.adapter = null
        _binding = null
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupPagerAdapter()
    }

    private fun setupPagerAdapter() {
        binding.pager.adapter =
            PaymentMainViewPagerAdapter(
                listOf(PaymentListFragment(), PaymentGraphFragment()),
                childFragmentManager,
                viewLifecycleOwner.lifecycle,
            )
        setupTabs()
    }

    private fun setupTabs() {
        val tabs = resources.getStringArray(R.array.payment_main_tabs)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position -> tab.text = tabs[position] }.attach()
    }
}
