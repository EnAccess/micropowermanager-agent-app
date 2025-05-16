package com.inensus.feature_main.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.Observer
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core.utils.AmountUtils
import com.inensus.core_ui.BaseFragment
import com.inensus.core_ui.extentions.animateGone
import com.inensus.core_ui.extentions.animateShow
import com.inensus.core_ui.extentions.gone
import com.inensus.core_ui.extentions.hide
import com.inensus.core_ui.extentions.show
import com.inensus.feature_main.R
import com.inensus.feature_main.databinding.FragmentDrawerBinding
import com.inensus.feature_main.viewmodel.DrawerViewModel
import com.inensus.shared_navigation.Feature
import com.inensus.shared_navigation.SharedNavigation
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal

class DrawerFragment : BaseFragment() {
    private val headerBinding get() = binding.drawerHeader
    private val viewModel: DrawerViewModel by viewModel()
    private val preferences: SharedPreferenceWrapper by inject()
    private val navigation: SharedNavigation by inject()

    var navCallback: ((action: NavigationAction) -> Unit)? = null

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentDrawerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDrawerBinding.inflate(inflater, container, false)
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

    private fun observeUiState() {
        viewModel.uiState.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    DrawerUiState.AgentLoading -> handleAgentLoading()
                    DrawerUiState.AgentError -> handleAgent()
                    is DrawerUiState.AgentLoaded -> handleAgentLoaded(it.email, it.balance)
                }
            },
        )

        viewModel.currentNavigation.observe(
            viewLifecycleOwner,
            Observer {
                navCallback?.invoke(it)
            },
        )

        viewModel.navigationUiState.observe(
            viewLifecycleOwner,
            Observer {
                setSelectedActionItem(it)
            },
        )
    }

    private fun handleAgent() {
        binding.drawerHeaderSkeleton.root.gone()
        binding.drawerHeaderError.root.show()
        binding.drawerHeader.root.hide()
    }

    private fun handleAgentLoading() {
        binding.drawerHeaderSkeleton.root.show()
        binding.drawerHeaderError.root.gone()
        binding.drawerHeader.root.hide()
    }

    private fun handleAgentLoaded(
        email: String,
        balance: BigDecimal,
    ) {
        binding.drawerHeaderSkeleton.root.animateGone()
        binding.drawerHeader.root.animateShow()
        headerBinding.emailText.text = email
        headerBinding.balanceText.text =
            getString(R.string.balance, AmountUtils.convertAmountToString(balance), getString(R.string.default_currency))
    }

    private fun setupView() {
        binding.itemDashboard.isItemSelected = true

        view?.findViewById<ImageButton>(R.id.retryButton)?.setOnClickListener { viewModel.getMe() }

        binding.itemDashboard.setOnClickListener { viewModel.onActionItemTapped(NavigationAction.OpenDashboard) }
        binding.itemCustomers.setOnClickListener { viewModel.onActionItemTapped(NavigationAction.OpenCustomers) }
        binding.itemPayments.setOnClickListener { viewModel.onActionItemTapped(NavigationAction.OpenPayments) }
        binding.itemAppliances.setOnClickListener { viewModel.onActionItemTapped(NavigationAction.OpenAppliances) }
        binding.itemTickets.setOnClickListener { viewModel.onActionItemTapped(NavigationAction.OpenTickets) }

        binding.signOutText.setOnClickListener {
            signOut()
        }
    }

    fun updateMe() {
        viewModel.getMe()
    }

    private fun setSelectedActionItem(navigationAction: NavigationAction) {
        binding.itemDashboard.isItemSelected = false
        binding.itemCustomers.isItemSelected = false
        binding.itemPayments.isItemSelected = false
        binding.itemAppliances.isItemSelected = false
        binding.itemTickets.isItemSelected = false

        when (navigationAction) {
            NavigationAction.OpenDashboard -> binding.itemDashboard
            NavigationAction.OpenCustomers -> binding.itemCustomers
            NavigationAction.OpenPayments -> binding.itemPayments
            NavigationAction.OpenAppliances -> binding.itemAppliances
            NavigationAction.OpenTickets -> binding.itemTickets
        }.isItemSelected = true
    }

    private fun signOut() {
        AlertDialog
            .Builder(context)
            .setTitle(getString(R.string.warning))
            .setCancelable(false)
            .setMessage(getString(R.string.warning_sign_out))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                preferences.accessToken = ""
                navigation.navigateTo(requireActivity(), Feature.Login)
            }.setNegativeButton(getString(R.string.no)) { _, _ -> }
            .show()
    }

    override fun provideViewModel() = viewModel

    companion object {
        fun newInstance() = DrawerFragment()
    }
}
