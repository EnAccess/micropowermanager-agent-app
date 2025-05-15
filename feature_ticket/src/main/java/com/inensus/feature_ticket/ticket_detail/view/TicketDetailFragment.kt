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
import com.inensus.feature_ticket.R
import com.inensus.feature_ticket.ticket_detail.viewmodel.TicketDetailViewModel
import kotlinx.android.synthetic.main.fragment_ticket_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TicketDetailFragment : BaseFragment() {
    private val viewModel: TicketDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.fragment_ticket_detail, container, false)

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
        rvTicketDetails.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = KeyValueAdapter(ticketDetails)
        }
    }

    companion object {
        const val EXTRA_TICKET_ID = "ticketId"
    }
}
