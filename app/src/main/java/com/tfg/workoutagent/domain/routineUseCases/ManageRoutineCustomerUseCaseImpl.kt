package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.data.repositories.RoutineRepository
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource

class ManageRoutineCustomerUseCaseImpl(private val repo: RoutineRepository) : ManageRoutineCustomerUseCase {
    override suspend fun getRoutine(routineId: String): Resource<Routine> = repo.getRoutine(routineId)
    override suspend fun getAssignedRoutine(): Resource<Routine> = repo.getAssignedRoutine()
}