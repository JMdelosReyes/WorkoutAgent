package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource

class ListCustomerUseCaseImpl(private val userRepository: UserRepository) : ListCustomerUseCase{
    override suspend fun getOwnCustomers(): Resource<MutableList<Customer>> = userRepository.getOwnCustomers()
}