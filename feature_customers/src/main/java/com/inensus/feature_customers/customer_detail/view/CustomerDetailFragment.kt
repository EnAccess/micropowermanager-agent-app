package com.inensus.feature_customers.customer_detail.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.inensus.core_ui.extentions.setupWithNavController
import com.inensus.feature_customers.R
import com.inensus.feature_customers.customer_detail.viewmodel.CustomerDetailViewModel
import kotlinx.android.synthetic.main.fragment_customer_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomerDetailFragment : Fragment() {
    private val viewModel: CustomerDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_customer_detail, container, false)
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

            bottomNavigation.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = childFragmentManager,
                containerId = R.id.content,
                intent = activity.intent,
            )
        }
    }
}
