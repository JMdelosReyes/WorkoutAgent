package com.tfg.workoutagent.domain.goalUseCases

import com.tfg.workoutagent.data.repositories.GoalRepository
import com.tfg.workoutagent.models.Goal
import com.tfg.workoutagent.vo.Resource

class ListGoalCustomerTrainerUseCaseImpl(private val repo : GoalRepository) : ListGoalCustomerTrainerUseCase {
    override suspend fun getGoalsByCustomerId(id: String): Resource<MutableList<Goal>> = repo.getGoalsByCustomerId(id)
}