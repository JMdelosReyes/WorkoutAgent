package com.tfg.workoutagent.domain.exercises

import com.tfg.workoutagent.data.repositories.ExerciseRepo
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

class ManageUseCaseImpl(private val repo: ExerciseRepo) : ManageUseCase {
    override suspend fun getExercise(id: String): Resource<Exercise> = repo.getExercise(id)

    override suspend fun createExercise(exercise: Exercise): Resource<Boolean> =
        repo.createExercise(exercise)

    override suspend fun editExercise(exercise: Exercise): Resource<Boolean> =
        repo.editExercise(exercise)

    override suspend fun deleteExercise(id: String): Resource<Boolean> = repo.deleteExercise(id)
}