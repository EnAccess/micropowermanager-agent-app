package com.inensus.feature_dashboard.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.inensus.core_ui.BaseActivity
import com.inensus.core_ui.extentions.*
import com.inensus.feature_dashboard.R
import com.inensus.feature_dashboard.databinding.FragmentDashboardMainBinding
import com.inensus.feature_dashboard.main.viewmodel.DashboardMainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardMainFragment : Fragment() {
    private val viewModel: DashboardMainViewModel by viewModel()

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentDashboardMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDashboardMainBinding.inflate(inflater, container, false)
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
        binding.loadingLayout.root.show()
        binding.content.hide()
        binding.errorLayout.root.gone()
        binding.loadingBottomNavigation.root.startShimmerAnimation()
        binding.bottomNavigation.hide()
    }

    private fun handleSuccessState() {
        setupBottomNavigation()

        binding.loadingBottomNavigation.root.stopShimmerAnimation()
        binding.loadingLayout.root.animateGone()
        binding.content.animateShow()
        binding.errorLayout.root.gone()
        binding.bottomNavigation.animateShow()
    }

    private fun handleErrorState() {
        binding.loadingLayout.root.animateGone()
        binding.content.gone()
        binding.bottomNavigation.gone()
        binding.loadingBottomNavigation.root.stopShimmerAnimation()
        binding.errorLayout.root.animateShow()
    }

    private fun setupBottomNavigation() {
        activity?.let { activity ->
            val navGraphIds =
                listOf(
                    R.navigation.dashboard_summary_navigation,
                    R.navigation.dashboard_graph_navigation,
                )

            binding.bottomNavigation.setupWithNavController(
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
