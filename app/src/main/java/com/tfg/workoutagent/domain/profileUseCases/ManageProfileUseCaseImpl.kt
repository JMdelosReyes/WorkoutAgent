package com.tfg.workoutagent.domain.profileUseCases

import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

class ManageProfileUseCaseImpl(private val repo: UserRepository) : ManageProfileUseCase {
    override suspend fun editProfileAdmin(): Resource<Boolean> = repo.updateProfileAdmin()
    override suspend fun editProfileCustomer(): Resource<Boolean> = repo.updateProfileCustomer()
    override suspend fun editProfileTrainer(): Resource<Boolean>  = repo.updateProfileTrainer()
    override suspend fun getLoggedUserTrainer(): Resource<Trainer> = repo.getLoggedUserTrainer()
    override suspend fun getLoggedUserCustomer(): Resource<Customer> = repo.getLoggedUserCustomer()
}