package com.inensus.feature_ticket.ticket_detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.inensus.core_ui.BaseFragment
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.core_ui.key_value.KeyValueAdapter
import com.inensus.feature_ticket.databinding.FragmentTicketDetailBinding
import com.inensus.feature_ticket.ticket_detail.viewmodel.TicketDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TicketDetailFragment : BaseFragment() {
    private val viewModel: TicketDetailViewModel by viewModel()

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentTicketDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTicketDetailBinding.inflate(inflater, container, false)
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

        arguments?.getString(EXTRA_TICKET_ID)?.let {
            viewModel.getTicketDetail(it)
        }

        observeTicketDetails()
    }

    override fun provideViewModel() = viewModel

    private fun observeTicketDetails() {
        viewModel.ticketDetails.observe(
            viewLifecycleOwner,
            Observer {
                setupTicketDetails(it)
            },
        )
    }

    private fun setupTicketDetails(ticketDetails: List<KeyValue>) {
        binding.rvTicketDetails.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = KeyValueAdapter(ticketDetails)
        }
    }

    companion object {
        const val EXTRA_TICKET_ID = "ticketId"
    }
}
