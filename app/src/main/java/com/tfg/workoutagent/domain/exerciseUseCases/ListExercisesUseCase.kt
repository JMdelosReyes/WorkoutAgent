package com.tfg.workoutagent.domain.exerciseUseCases

import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

interface ListExercisesUseCase {

    suspend fun getExercises(): Resource<MutableList<Exercise>>
}