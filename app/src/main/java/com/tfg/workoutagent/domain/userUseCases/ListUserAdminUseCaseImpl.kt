package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

class ListUserAdminUseCaseImpl(private val repo : UserRepository) : ListUserAdminUseCase {
    override suspend fun getCustomersAdmin(): Resource<MutableList<Customer>> = repo.getCustomersAdmin()
    override suspend fun getTrainersAdmin(): Resource<MutableList<Trainer>> = repo.getTrainersAdmin()
}