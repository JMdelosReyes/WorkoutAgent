package com.tfg.workoutagent.domain.goalUseCases

import com.tfg.workoutagent.data.repositories.GoalRepository
import com.tfg.workoutagent.models.Goal
import com.tfg.workoutagent.vo.Resource

class ManageGoalUseCaseImpl(private val repo : GoalRepository) : ManageGoalUseCase{
    override suspend fun createGoal(goal: Goal): Resource<Boolean> = repo.createGoal(goal)
}