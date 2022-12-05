package com.inensus.feature_ticket.ticket_list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inensus.core_network.model.Customer
import com.inensus.core_ui.BaseActivity
import com.inensus.core_ui.extentions.*
import com.inensus.core_ui.util.KeyboardUtils
import com.inensus.feature_ticket.R
import com.inensus.feature_ticket.ticket_detail.view.TicketDetailFragment
import com.inensus.feature_ticket.ticket_list.model.Ticket
import com.inensus.feature_ticket.ticket_list.viewmodel.TicketListViewModel
import kotlinx.android.synthetic.main.fragment_ticket_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TicketListFragment : Fragment() {

    private val viewModel: TicketListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_ticket_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeUiState()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.saveTicketsState((rvTickets.adapter as TicketListAdapter).tickets)
    }

    private fun setupView() {
        view?.findViewById<Button>(R.id.btnRetry)?.setOnClickListener {
            viewModel.getTickets(type = LoadingTicketListType.INITIAL)
        }

        createTicket.setOnClickListener {
            (activity as BaseActivity).provideNavController().navigate(R.id.openTicketForm)
        }

        rvTickets.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TicketListAdapter().apply {
                onItemClick = {
                    viewModel.onTicketTapped(it)
                }
            }

            addOnScrollListener(setupPagination())
        }
    }

    private fun setupPagination() =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val adapter = (rvTickets.adapter as TicketListAdapter)

                if (lastItemPosition + LIST_THRESHOLD > adapter.tickets.size && adapter.tickets.isNotEmpty()) {
                    viewModel.getTickets(type = LoadingTicketListType.PAGINATE)
                }
            }
        }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is TicketListUiState.Loading -> handleLoading(it.type)
                TicketListUiState.Error -> handleError()
                TicketListUiState.NoMoreData -> handleNoMore()
                TicketListUiState.Empty -> handleEmpty()
                is TicketListUiState.Success -> handleSuccess(it.tickets, it.type)
                is TicketListUiState.TicketTapped -> handleTicketTapped(it.ticketId)
            }
        })

        viewModel.tickets.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                viewModel.getTickets(type = LoadingTicketListType.INITIAL)
            } else {
                handleSuccess(it, LoadingTicketListType.INITIAL)
            }
        })

        viewModel.customer.observe(viewLifecycleOwner, Observer {
            updateView(it)
        })
    }

    private fun handleLoading(type: LoadingTicketListType) {
        if (type == LoadingTicketListType.PAGINATE) {
            progressBar.show()
        } else {
            rvTickets.gone()
            loadingLayout.show()
        }
        errorLayout.gone()
        emptyLayout.gone()
    }

    private fun handleError() {
        errorLayout.animateShow()
        loadingLayout.animateGone()
        emptyLayout.gone()
        progressBar.hide()
        rvTickets.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleEmpty() {
        emptyLayout.animateShow()
        loadingLayout.animateGone()
        errorLayout.gone()
        progressBar.hide()
        (rvTickets.adapter as TicketListAdapter).tickets = emptyList()
        rvTickets.gone()

        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleNoMore() {
        progressBar.hide()
        KeyboardUtils.hideKeyboard(requireActivity())
    }

    private fun handleSuccess(tickets: List<Ticket>, loadCustomerType: LoadingTicketListType) {
        if (tickets != (rvTickets.adapter as TicketListAdapter).tickets) {
            progressBar.hide()
            rvTickets.animateShow()
            updateTicketsData(tickets, loadCustomerType)
            loadingLayout.animateGone()
        }
    }

    private fun updateTicketsData(tickets: List<Ticket>, loadCustomerType: LoadingTicketListType) {
        val adapter = (rvTickets.adapter as TicketListAdapter)

        if (loadCustomerType == LoadingTicketListType.PAGINATE) {
            adapter.updateData(ArrayList(adapter.tickets).apply { addAll(tickets) })
        } else {
            rvTickets.adapter = TicketListAdapter().apply {
                onItemClick = {
                    viewModel.onTicketTapped(it)
                }

                this.tickets = tickets
                notifyDataSetChanged()
            }
        }

        viewModel.saveTicketsState((rvTickets.adapter as TicketListAdapter).tickets)
    }

    private fun handleTicketTapped(ticketId: String) {
        (activity as BaseActivity).provideNavController().navigate(
            R.id.openTicketDetail,
            bundleOf(TicketDetailFragment.EXTRA_TICKET_ID to ticketId)
        )
    }

    private fun updateView(customer: Customer?) {
        createTicket.visibility = if (customer != null) View.VISIBLE else View.GONE
        view?.findViewById<TextView>(R.id.tvEmptyDescription)?.text =
            if (customer != null) getString(
                R.string.ticket_empty_layout__with_customer_content,
                customer.name
            ) else getString(R.string.ticket_empty_layout_content)
    }

    companion object {
        private const val LIST_THRESHOLD = 2
    }
}