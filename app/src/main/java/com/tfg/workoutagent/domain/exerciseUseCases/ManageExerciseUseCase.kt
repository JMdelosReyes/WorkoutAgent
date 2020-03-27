package com.tfg.workoutagent.domain.exerciseUseCases

import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

interface ManageExerciseUseCase {
    suspend fun getExercise(id: String): Resource<Exercise>
    suspend fun createExercise(exercise: Exercise): Resource<Boolean>
    suspend fun editExercise(exercise: Exercise): Resource<Boolean>
    suspend fun deleteExercise(id: String): Resource<Boolean>
}