package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

class ManageTrainerAdminUseCaseImpl(private val repo: UserRepository) : ManageTrainerAdminUseCase {
    override suspend fun createTrainer(trainer: Trainer): Resource<Boolean> = repo.createTrainer(trainer)
}