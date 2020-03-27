package com.tfg.workoutagent.domain.exerciseUseCases

import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

interface DisplayExerciseUseCase {
    suspend fun getExercise(id: String): Resource<Exercise>
}