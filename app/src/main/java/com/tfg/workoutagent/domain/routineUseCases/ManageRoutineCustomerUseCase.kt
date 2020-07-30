package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.models.RoutineActivity
import com.tfg.workoutagent.vo.Resource

interface ManageRoutineCustomerUseCase {
    suspend fun getRoutine(routineId: String) : Resource<Routine>
    suspend fun getAssignedRoutine() : Resource<Routine>
    suspend fun getTodayActivities() : Resource<MutableList<RoutineActivity>>
}