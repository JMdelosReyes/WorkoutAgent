package com.tfg.workoutagent.domain.profileUseCases

import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase
import com.tfg.workoutagent.models.Administrator
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

class DisplayProfileUserUseCaseImpl(private val repo : UserRepository) :
    DisplayProfileUserUseCase {
    override suspend fun getLoggedUserTrainer(): Resource<Trainer> = repo.getLoggedUserTrainer()
    override suspend fun getLoggedUserCustomer(): Resource<Customer> = repo.getLoggedUserCustomer()
    override suspend fun getLoggedUserAdmin(): Resource<Administrator> = repo.getLoggedUserAdmin()
}