package com.tfg.workoutagent.domain.loginUseCases

import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

interface LoginUseCase {
    suspend fun getRole() : Resource<String>
    suspend fun updateToken(): Resource<String>
    suspend fun getTrainerByCustomerId(): Resource<Trainer>
    suspend fun getIdTrainerByCustomerId(): Resource<String>
    suspend fun getLoggedUserTrainer(): Resource<Trainer>
}