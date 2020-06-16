package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource

class ManageCustomerAdminUseCaseImpl(private val repo : UserRepository) : ManageCustomerAdminUseCase {
    override suspend fun getCustomer(id: String): Resource<Customer> = repo.getCustomer(id)
    override suspend fun deleteCustomerAdmin(id: String): Resource<Boolean> = repo.deleteCustomerAdmin(id)
}