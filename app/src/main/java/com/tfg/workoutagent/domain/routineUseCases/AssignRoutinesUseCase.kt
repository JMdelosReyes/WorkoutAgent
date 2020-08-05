package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource

interface AssignRoutinesUseCase {
    suspend fun getTemplateRoutines(): Resource<MutableList<Routine>>
}