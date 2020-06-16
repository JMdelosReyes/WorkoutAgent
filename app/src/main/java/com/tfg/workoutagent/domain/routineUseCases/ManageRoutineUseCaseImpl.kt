package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.data.repositories.ExerciseRepository
import com.tfg.workoutagent.data.repositories.RoutineRepository
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource

class ManageRoutineUseCaseImpl(
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository
) :
    ManageRoutineUseCase {
    override suspend fun getRoutine(id: String): Resource<Routine> =
        routineRepository.getRoutine(id)

    override suspend fun createRoutine(routine: Routine): Resource<Boolean> =
        routineRepository.createRoutine(routine)

    override suspend fun editRoutine(routine: Routine): Resource<Boolean> {
        return routineRepository.editRoutine(routine)
    }

    override suspend fun deleteRoutine(id: String): Resource<Boolean> {
        return routineRepository.deleteRoutine(id)
    }

    override suspend fun getExercises(): Resource<List<Exercise>> =
        exerciseRepository.getExercises()
}