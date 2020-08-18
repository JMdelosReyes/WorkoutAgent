package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.data.repositories.RoutineRepository
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource

class HistoricRoutinesUseCaseImpl(private val repo: RoutineRepository) : HistoricRoutinesUseCase {
    override suspend fun getHistoricRoutinesByCustomerId(customerId: String): Resource<MutableList<Routine>> = repo.getRoutinesByCustomerId(customerId)
    override suspend fun getHistoricRoutineById(routineId: String): Resource<Routine> = repo.getRoutine(routineId)
}