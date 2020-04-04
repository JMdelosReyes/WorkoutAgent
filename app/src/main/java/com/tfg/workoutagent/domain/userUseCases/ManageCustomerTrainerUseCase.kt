package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource

interface ManageCustomerTrainerUseCase {
    suspend fun createCustomer(customer : Customer): Resource<Boolean>
}