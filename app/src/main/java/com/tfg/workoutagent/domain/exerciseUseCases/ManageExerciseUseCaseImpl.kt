package com.tfg.workoutagent.domain.exerciseUseCases

import com.tfg.workoutagent.data.repositories.ExerciseRepository
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

class ManageExerciseUseCaseImpl(private val repo: ExerciseRepository) : ManageExerciseUseCase {
    override suspend fun getExercise(id: String): Resource<Exercise> = repo.getExercise(id)

    override suspend fun createExercise(exercise: Exercise): Resource<Boolean> =
        repo.createExercise(exercise)

    override suspend fun editExercise(exercise: Exercise): Resource<Boolean> =
        repo.editExercise(exercise)

    override suspend fun deleteExercise(id: String): Resource<Boolean> = repo.deleteExercise(id)
}