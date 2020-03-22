package com.tfg.workoutagent.data.repositories

import com.google.firebase.firestore.DocumentSnapshot
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

interface ExerciseRepo {

    suspend fun getExercises(): Resource<MutableList<Exercise>>

    suspend fun getExercise(id: String): Resource<Exercise>
}