package com.tfg.workoutagent.data.repoimpl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.tfg.workoutagent.data.repositories.ExerciseRepo
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await

class ExerciseRepoImpl : ExerciseRepo {

    override suspend fun getExercises(): Resource<MutableList<Exercise>> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("exercises").get().await()

        val exercises = mutableListOf<Exercise>()
        for (document: QueryDocumentSnapshot in resultData) {
            val exercise = document.toObject(Exercise::class.java)
            exercise.id = document.id
            exercises.add(exercise)
        }
        return Resource.Success(exercises!!)
    }

    override suspend fun getExercise(id: String): Resource<Exercise> {
        val exerciseData =
            FirebaseFirestore.getInstance().collection("exercises").document(id).get().await()
        val exercise = exerciseData.toObject(Exercise::class.java)
        exercise!!.id = exerciseData.id
        return Resource.Success(exercise)
    }
}