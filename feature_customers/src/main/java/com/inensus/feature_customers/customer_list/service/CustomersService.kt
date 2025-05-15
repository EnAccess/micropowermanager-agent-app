package com.inensus.feature_customers.customer_list.service

import com.inensus.feature_customers.customer_list.model.CustomerResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface CustomersService {
    @GET
    fun getCustomers(
        @Url url: String,
    ): Single<CustomerResponse>
}
