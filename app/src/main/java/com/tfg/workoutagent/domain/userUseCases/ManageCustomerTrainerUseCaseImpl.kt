package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource

class ManageCustomerTrainerUseCaseImpl(private val repository: UserRepository) : ManageCustomerTrainerUseCase {
    override suspend fun createCustomer(customer: Customer): Resource<Boolean> = repository.createCustomer(customer)
}