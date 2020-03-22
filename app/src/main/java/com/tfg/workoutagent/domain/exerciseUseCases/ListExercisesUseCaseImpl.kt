package com.tfg.workoutagent.domain.exerciseUseCases

import com.tfg.workoutagent.data.repositories.ExerciseRepository
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

class ListExercisesUseCaseImpl(private val repo:ExerciseRepository): ListExercisesUseCase {

    override suspend fun getExercises(): Resource<MutableList<Exercise>> = repo.getExercises()
}