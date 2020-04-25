package com.tfg.workoutagent.domain.profileUseCases

import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Administrator
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

class ManageProfileUseCaseImpl(private val repo: UserRepository) : ManageProfileUseCase {
    override suspend fun editProfileAdmin(administrator: Administrator): Resource<Boolean> = repo.updateProfileAdmin(administrator)
    override suspend fun editProfileCustomer(customer: Customer): Resource<Boolean> = repo.updateProfileCustomer(customer)
    override suspend fun editProfileTrainer(trainer: Trainer): Resource<Boolean>  = repo.updateProfileTrainer(trainer)
    override suspend fun getLoggedUserTrainer(): Resource<Trainer> = repo.getLoggedUserTrainer()
    override suspend fun getLoggedUserCustomer(): Resource<Customer> = repo.getLoggedUserCustomer()
    override suspend fun deleteLoggedTrainer(): Resource<Boolean> = repo.deleteLoggedTrainer()
    override suspend fun deleteLoggedCustomer(): Resource<Boolean> = repo.deleteLoggedCustomer()
}