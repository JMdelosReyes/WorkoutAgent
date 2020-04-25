package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource

interface ManageCustomerAdminUseCase {
    suspend fun getCustomer(id: String) : Resource<Customer>
    suspend fun deleteCustomerAdmin(id: String) : Resource<Boolean>
}