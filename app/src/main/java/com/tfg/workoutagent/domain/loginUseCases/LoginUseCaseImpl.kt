package com.tfg.workoutagent.domain.loginUseCases

import com.tfg.workoutagent.data.repositories.LoginRepository
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

class LoginUseCaseImpl(private val repository: LoginRepository) : LoginUseCase{
    override suspend fun getRole(): Resource<String> = repository.getRole()
    override suspend fun updateToken(): Resource<String> = repository.updateToken()
    override suspend fun getTrainerByCustomerId(): Resource<Trainer> = repository.getTrainerByCustomerId()
    override suspend fun getIdTrainerByCustomerId(): Resource<String> = repository.getIdTrainerByCustomerId()
    override suspend fun getLoggedUserTrainer(): Resource<Trainer> = repository.getLoggedUserTrainer()
}