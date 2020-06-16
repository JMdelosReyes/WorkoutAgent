package com.tfg.workoutagent.domain.routineUseCases


import com.tfg.workoutagent.data.repositories.RoutineRepository
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource

class ListRoutinesUseCaseImpl(private val repo:RoutineRepository):
    ListRoutinesUseCase {

    override suspend fun getOwnRoutines(): Resource<MutableList<Routine>> = repo.getOwnRoutines()
}