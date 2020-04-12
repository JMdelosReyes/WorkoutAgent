package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

interface ManageTrainerAdminUseCase {
    suspend fun createTrainer(trainer:Trainer) : Resource<Boolean>
}