package com.tfg.workoutagent.domain.exercises

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.workoutagent.data.repositories.ExerciseRepo
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource

class ListExercisesImpl(private val repo:ExerciseRepo): ListExercises {

    override suspend fun getExercises(): Resource<MutableList<Exercise>> = repo.getExercises()
}