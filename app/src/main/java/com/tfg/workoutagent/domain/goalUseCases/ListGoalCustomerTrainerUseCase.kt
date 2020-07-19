package com.tfg.workoutagent.domain.goalUseCases

import com.tfg.workoutagent.models.Goal
import com.tfg.workoutagent.vo.Resource

interface ListGoalCustomerTrainerUseCase {
    suspend fun getGoalsByCustomerId(id: String) : Resource<MutableList<Goal>>
}