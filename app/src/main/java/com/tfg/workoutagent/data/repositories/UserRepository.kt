package com.tfg.workoutagent.data.repositories

import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource

interface UserRepository {

    suspend fun getOwnCustomers(): Resource<MutableList<Customer>>

    suspend fun getCustomer(id: String) : Resource<Customer>

    suspend fun createCustomer(customer: Customer) : Resource<Boolean>

    suspend fun updateCustomer(customer: Customer) : Resource<Boolean>

    suspend fun deleteCustomer(id : String): Resource<Boolean>
}