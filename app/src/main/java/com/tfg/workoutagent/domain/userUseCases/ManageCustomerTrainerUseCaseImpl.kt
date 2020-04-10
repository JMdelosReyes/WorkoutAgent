package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource

class ManageCustomerTrainerUseCaseImpl(private val repository: UserRepository) : ManageCustomerTrainerUseCase {
    override suspend fun createCustomer(customer: Customer): Resource<Boolean> = repository.createCustomer(customer)
    override suspend fun getCustomer(id: String): Resource<Customer> = repository.getCustomer(id)
    override suspend fun getOwnCustomers(): Resource<MutableList<Customer>> = repository.getOwnCustomers()
    override suspend fun deleteCustomer(id: String): Resource<Boolean> = repository.deleteCustomer(id)
    override suspend fun updateCustomer(customer: Customer): Resource<Boolean> = repository.updateCustomer(customer)
}