package com.inensus.feature_payment.payment_main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PaymentMainViewPagerAdapter(
    private val fragments: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
