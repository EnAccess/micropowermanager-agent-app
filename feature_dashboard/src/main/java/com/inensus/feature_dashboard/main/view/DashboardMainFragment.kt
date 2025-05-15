package com.inensus.feature_dashboard.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.inensus.core_ui.BaseActivity
import com.inensus.core_ui.extentions.*
import com.inensus.core_ui.shimmer_layout.ShimmerLayout
import com.inensus.feature_dashboard.R
import com.inensus.feature_dashboard.main.viewmodel.DashboardMainViewModel
import kotlinx.android.synthetic.main.fragment_dashboard_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardMainFragment : Fragment() {
    private val viewModel: DashboardMainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_dashboard_main, container, false)

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
            viewModel.getDashboardData()
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner, {
            when (it) {
                DashboardMainUiState.Loading -> handleLoadingState()
                DashboardMainUiState.Success -> handleSuccessState()
                DashboardMainUiState.Error -> handleErrorState()
            }
        })

        viewModel.messagingReceiverState.observe(viewLifecycleOwner, {
            observeMessagingReceiver()
        })
    }

    private fun handleLoadingState() {
        loadingLayout.show()
        content.hide()
        errorLayout.gone()
        (loadingBottomNavigation as ShimmerLayout).startShimmerAnimation()
        bottomNavigation.hide()
    }

    private fun handleSuccessState() {
        setupBottomNavigation()

        (loadingBottomNavigation as ShimmerLayout).stopShimmerAnimation()
        loadingLayout.animateGone()
        content.animateShow()
        errorLayout.gone()
        bottomNavigation.animateShow()
    }

    private fun handleErrorState() {
        loadingLayout.animateGone()
        content.gone()
        bottomNavigation.gone()
        (loadingBottomNavigation as ShimmerLayout).stopShimmerAnimation()
        errorLayout.animateShow()
    }

    private fun setupBottomNavigation() {
        activity?.let { activity ->
            val navGraphIds =
                listOf(
                    R.navigation.dashboard_summary_navigation,
                    R.navigation.dashboard_graph_navigation,
                )

            bottomNavigation.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = childFragmentManager,
                containerId = R.id.content,
                intent = activity.intent,
            )
        }
    }

    private fun observeMessagingReceiver() {
        (requireActivity() as BaseActivity).messagingReceiver.event.observe(this, {
            viewModel.getDashboardData()
        })
    }
}
