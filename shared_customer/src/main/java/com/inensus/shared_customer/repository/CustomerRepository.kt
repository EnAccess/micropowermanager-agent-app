package com.inensus.shared_customer.repository

import com.inensus.core_network.model.Customer

class CustomerRepository {
    var customer: Customer? = null

    fun clearCustomer() {
        customer = null
    }
}
