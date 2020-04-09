package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.data.repositories.RoutineRepository
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource

class ManageRoutineUseCaseImpl(private val repo: RoutineRepository) : ManageRoutineUseCase {
    override suspend fun getRoutine(id: String): Resource<Routine> = repo.getRoutine(id)

    override suspend fun createRoutine(routine: Routine): Resource<Boolean> = repo.createRoutine(routine)

    override suspend fun editRoutine(routine: Routine): Resource<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteRoutine(id: String): Resource<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}