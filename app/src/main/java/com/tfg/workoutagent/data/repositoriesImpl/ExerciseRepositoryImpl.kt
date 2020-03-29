package com.tfg.workoutagent.data.repositoriesImpl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.tfg.workoutagent.data.repositories.ExerciseRepository
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await

class ExerciseRepositoryImpl : ExerciseRepository {

    override suspend fun getExercises(): Resource<MutableList<Exercise>> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("exercises").get().await()

        val exercises = mutableListOf<Exercise>()
        for (document: QueryDocumentSnapshot in resultData) {
            val exercise = document.toObject(Exercise::class.java)
            exercise.id = document.id
            exercises.add(exercise)
        }
        return Resource.Success(exercises)
    }

    override suspend fun getExercise(id: String): Resource<Exercise> {
        val exerciseData =
            FirebaseFirestore.getInstance().collection("exercises").document(id).get().await()
        val exercise = exerciseData.toObject(Exercise::class.java)
        exercise!!.id = exerciseData.id
        return Resource.Success(exercise)
    }

    override suspend fun createExercise(exercise: Exercise): Resource<Boolean> {
        val data = hashMapOf(
            "title" to exercise.title,
            "description" to exercise.description,
            "photos" to exercise.photos,
            "tags" to exercise.tags
        )
        FirebaseFirestore.getInstance().collection("exercises").add(data).await()
        return Resource.Success(true)
    }

    override suspend fun editExercise(exercise: Exercise): Resource<Boolean> {
        val data = hashMapOf(
            "title" to exercise.title,
            "description" to exercise.description,
            "photos" to exercise.photos,
            "tags" to exercise.tags
        )
        FirebaseFirestore.getInstance().collection("exercises").document(exercise.id).update(data)
            .await()
        return Resource.Success(true)
    }

    override suspend fun deleteExercise(id: String): Resource<Boolean> {
        FirebaseFirestore.getInstance().collection("exercises").document(id).delete().await()
        return Resource.Success(true)
    }
}