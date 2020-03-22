package com.tfg.workoutagent.domain.exercises

import com.google.firebase.firestore.DocumentSnapshot
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

interface ListExercises {

    suspend fun getExercises(): Resource<MutableList<Exercise>>
}