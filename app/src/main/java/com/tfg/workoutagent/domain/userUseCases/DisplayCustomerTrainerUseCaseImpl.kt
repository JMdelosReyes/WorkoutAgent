package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource

class DisplayCustomerTrainerUseCaseImpl(private val repo: UserRepository) : DisplayCustomerTrainerUseCase {
    override suspend fun getCustomer(id: String): Resource<Customer> = repo.getCustomer(id)
}