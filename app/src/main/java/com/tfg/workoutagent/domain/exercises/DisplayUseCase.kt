package com.tfg.workoutagent.domain.exercises

import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

interface DisplayUseCase {
    suspend fun getExercise(id: String): Resource<Exercise>
}