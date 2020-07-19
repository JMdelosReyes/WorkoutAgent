package com.tfg.workoutagent.data.repositoriesImpl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.workoutagent.data.repositories.GoalRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Goal
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await

class GoalRepositoryImpl: GoalRepository {
    override suspend fun getGoalsCustomer(): Resource<MutableList<Goal>> {
        val document = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0]
        var goals = mutableListOf<Goal>()
        val customer = document.toObject(Customer::class.java)
        if(!customer!!.goals.isEmpty()){
            goals = customer.goals.asReversed()
        }
        return Resource.Success(goals)
    }

    override suspend fun createGoal(goal: Goal) : Resource<Boolean> {
        val id = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0].id
        FirebaseFirestore.getInstance().collection("users").document(id).update("goals", FieldValue.arrayUnion(hashMapOf(
            "aim" to goal.aim, "description" to goal.description, "isAchieved" to false, "startDate" to goal.startDate, "endDate" to goal.endDate
        ))).await()
        return Resource.Success(true)
    }
    override suspend fun deleteGoal(index: Int) : Resource<Boolean> {
        val customerDoc = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0]
        val customer = customerDoc.toObject(Customer::class.java)!!
        customer.id = customerDoc.id
        customer.goals.asReversed().removeAt(index)
        Log.i("deleteGoal", "PARA PUNTO DE RUPTURA")
        val data : HashMap<String, Any?> = hashMapOf("birthday" to customer.birthday, "dni" to customer.dni, "genre" to customer.genre, "email" to customer.email, "name" to customer.name, "surname" to customer.surname, "goals" to customer.goals, "photo" to customer.photo, "height" to customer.height, "phone" to customer.phone, "role" to "CUSTOMER", "weightPerWeek" to customer.weightPerWeek, "weights" to customer.weights)
        FirebaseFirestore.getInstance().collection("users").document(customer.id).update(data).await()
        return Resource.Success(true)
    }

    override suspend fun finishGoal(index: Int): Resource<Boolean> {
        val customerDoc = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0]
        val customer = customerDoc.toObject(Customer::class.java)!!
        customer.id = customerDoc.id
        customer.goals.asReversed().get(index).isAchieved = true
        Log.i("finishGoal", "PARA PUNTO DE RUPTURA")
        val data : HashMap<String, Any?> = hashMapOf("birthday" to customer.birthday, "dni" to customer.dni, "genre" to customer.genre, "email" to customer.email, "name" to customer.name, "surname" to customer.surname, "goals" to customer.goals, "photo" to customer.photo, "height" to customer.height, "phone" to customer.phone, "role" to "CUSTOMER", "weightPerWeek" to customer.weightPerWeek, "weights" to customer.weights)
        FirebaseFirestore.getInstance().collection("users").document(customer.id).update(data).await()
        return Resource.Success(true)
    }
}