package com.tfg.workoutagent.data.repositories

import com.tfg.workoutagent.models.Goal
import com.tfg.workoutagent.vo.Resource

interface GoalRepository {
    suspend fun getGoalsCustomer() : Resource<MutableList<Goal>>
    suspend fun createGoal(goal: Goal): Resource<Boolean>
    suspend fun deleteGoal(index: Int): Resource<Boolean>
}