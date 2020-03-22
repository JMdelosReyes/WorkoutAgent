package com.tfg.workoutagent.domain.exercises

import com.tfg.workoutagent.data.repositories.ExerciseRepo
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

class DisplayUseCaseImpl(private val repo: ExerciseRepo) : DisplayUseCase {
    override suspend fun getExercise(id: String): Resource<Exercise> = repo.getExercise(id)
}