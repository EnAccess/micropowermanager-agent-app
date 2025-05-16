package com.inensus.feature_customers.customer_detail.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.inensus.core_ui.extentions.setupWithNavController
import com.inensus.feature_customers.R
import com.inensus.feature_customers.customer_detail.viewmodel.CustomerDetailViewModel
import com.inensus.feature_customers.databinding.FragmentCustomerDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomerDetailFragment : Fragment() {
    private val viewModel: CustomerDetailViewModel by viewModel()

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentCustomerDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentCustomerDetailBinding.inflate(inflater, container, false)
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

        setupView()
        observeCustomer()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupView() {
        setupBottomNavigation()
    }

    private fun observeCustomer() {
        viewModel.customer.observe(
            viewLifecycleOwner,
            Observer {
                setTitle("${it.name} ${it.surname}")
            },
        )
    }

    private fun setTitle(title: String) {
        (activity as AppCompatActivity?)?.supportActionBar?.title = title
    }

    private fun setupBottomNavigation() {
        activity?.let { activity ->
            val navGraphIds =
                listOf(
                    R.navigation.payment_navigation,
                    R.navigation.appliance_navigation,
                    R.navigation.ticket_navigation,
                )

            binding.bottomNavigation.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = childFragmentManager,
                containerId = R.id.content,
                intent = activity.intent,
            )
        }
    }
}
