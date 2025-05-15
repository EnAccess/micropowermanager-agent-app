package com.inensus.feature_payment.payment_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.inensus.feature_payment.R
import com.inensus.feature_payment.payment_graph.view.PaymentGraphFragment
import com.inensus.feature_payment.payment_list.view.PaymentListFragment
import kotlinx.android.synthetic.main.fragment_payment_main.*

class PaymentMainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_payment_main, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupPagerAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        pager.adapter = null
    }

    private fun setupPagerAdapter() {
        pager.adapter =
            PaymentMainViewPagerAdapter(
                listOf(PaymentListFragment(), PaymentGraphFragment()),
                childFragmentManager,
                viewLifecycleOwner.lifecycle,
            )
        setupTabs()
    }

    private fun setupTabs() {
        val tabs = resources.getStringArray(R.array.payment_main_tabs)
        TabLayoutMediator(tabLayout, pager) { tab, position -> tab.text = tabs[position] }.attach()
    }
}
