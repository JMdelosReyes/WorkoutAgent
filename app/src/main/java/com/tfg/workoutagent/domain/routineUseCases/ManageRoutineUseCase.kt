package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource

interface ManageRoutineUseCase {

    suspend fun getRoutine(id: String): Resource<Routine>
    suspend fun createRoutine(routine: Routine): Resource<Boolean>
    suspend fun editRoutine(routine: Routine): Resource<Boolean>
    suspend fun deleteRoutine(id: String): Resource<Boolean>
}