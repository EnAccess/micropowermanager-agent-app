package com.inensus.feature_customers.customer_list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.inensus.core_network.model.Customer
import com.inensus.core_ui.extentions.createInitialsDrawable
import com.inensus.feature_customers.R
import kotlinx.android.synthetic.main.customer_list_item.view.*

class CustomerListAdapter : RecyclerView.Adapter<CustomerListAdapter.ViewHolder>() {

    lateinit var onItemClick: ((customer: Customer) -> Unit)
    var customers: List<Customer> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.customer_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(customers[position])
    }

    override fun getItemCount() = customers.size

    fun updateData(newItems: List<Customer>) {
        val diff = DiffUtil.calculateDiff(CustomerDiffUtils(newItems, customers))
        diff.dispatchUpdatesTo(this)
        customers = newItems
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(customer: Customer) {
            with(itemView) {
                customerImage.setImageDrawable(
                    createInitialsDrawable(
                        context.getString(R.string.customer_name_surname, customer.name, customer.surname),
                        ContextCompat.getColor(context, R.color.white),
                        ContextCompat.getColor(context, R.color.colorPrimary)
                    )
                )
                customerNameText.text = context.getString(R.string.customer_name_surname, customer.name, customer.surname)
                if (customer.addresses.isNotEmpty()) {
                    customerPhoneNumberText.text = customer.addresses[0].phone
                    customerCityText.text = customer.addresses[0].city.name
                }

                setOnClickListener {
                    onItemClick.invoke(customer)
                }
            }
        }
    }
}