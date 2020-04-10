package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource

interface ListRoutinesUseCase {

    suspend fun getOwnRoutines(): Resource<MutableList<Routine>>
}