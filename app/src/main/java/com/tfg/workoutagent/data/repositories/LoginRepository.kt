package com.tfg.workoutagent.data.repositories

import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

interface LoginRepository {
    suspend fun getRole() : Resource<String>
    suspend fun updateToken(): Resource<String>
    suspend fun getTrainerByCustomerId(): Resource<Trainer>
    suspend fun getLoggedUserTrainer(): Resource<Trainer>
}