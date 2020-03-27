package com.tfg.workoutagent.data.repositories

import com.google.firebase.firestore.DocumentSnapshot
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

interface ExerciseRepository {

    suspend fun getExercises(): Resource<MutableList<Exercise>>

    suspend fun getExercise(id: String): Resource<Exercise>

    suspend fun createExercise(exercise: Exercise): Resource<Boolean>

    suspend fun editExercise(exercise: Exercise): Resource<Boolean>

    suspend fun deleteExercise(id: String): Resource<Boolean>
}