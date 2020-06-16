package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource

interface ListCustomerUseCase {
    suspend fun getOwnCustomers() : Resource<MutableList<Customer>>
}