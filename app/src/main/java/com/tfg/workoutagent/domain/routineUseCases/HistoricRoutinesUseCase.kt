package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource

interface HistoricRoutinesUseCase {
    suspend fun getHistoricRoutinesByCustomerId(customerId: String) : Resource<MutableList<Routine>>
    suspend fun getHistoricRoutineById(routineId: String) : Resource<Routine>
}