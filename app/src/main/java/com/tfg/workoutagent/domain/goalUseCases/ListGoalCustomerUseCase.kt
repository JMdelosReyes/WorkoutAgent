package com.tfg.workoutagent.domain.goalUseCases

import com.tfg.workoutagent.models.Goal
import com.tfg.workoutagent.vo.Resource

interface ListGoalCustomerUseCase {
    suspend fun getGoalsCustomer() : Resource<MutableList<Goal>>
}