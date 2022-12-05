package com.inensus.shared_customer.di

import com.inensus.shared_customer.repository.CustomerRepository
import org.koin.dsl.module

object CustomerModule {
    fun createCustomerModule() = module { single { CustomerRepository() } }
}