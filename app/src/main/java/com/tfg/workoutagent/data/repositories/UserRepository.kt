package com.tfg.workoutagent.data.repositories

import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource

interface UserRepository {

    suspend fun getOwnCustomers(): Resource<MutableList<Customer>>
}