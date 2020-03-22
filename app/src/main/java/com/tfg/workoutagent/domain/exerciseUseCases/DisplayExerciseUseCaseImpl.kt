package com.tfg.workoutagent.domain.exerciseUseCases

import com.tfg.workoutagent.data.repositories.ExerciseRepository
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

class DisplayExerciseUseCaseImpl(private val repo: ExerciseRepository) : DisplayExerciseUseCase {
    override suspend fun getExercise(id: String): Resource<Exercise> = repo.getExercise(id)
}