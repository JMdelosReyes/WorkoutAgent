package com.tfg.workoutagent.domain.goalUseCases

import com.tfg.workoutagent.data.repositories.GoalRepository
import com.tfg.workoutagent.models.Goal
import com.tfg.workoutagent.vo.Resource

class ListGoalCustomerUseCaseImpl(private val repo : GoalRepository) : ListGoalCustomerUseCase {
    override suspend fun getGoalsCustomer(): Resource<MutableList<Goal>> = repo.getGoalsCustomer()
}