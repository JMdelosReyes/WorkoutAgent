package com.tfg.workoutagent.domain.goalUseCases

import com.tfg.workoutagent.models.Goal
import com.tfg.workoutagent.vo.Resource

interface ManageGoalUseCase {
    suspend fun createGoal(goal: Goal): Resource<Boolean>
}