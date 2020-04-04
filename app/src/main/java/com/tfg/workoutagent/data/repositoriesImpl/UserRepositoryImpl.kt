package com.tfg.workoutagent.data.repositoriesImpl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Goal
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository {

    override suspend fun getOwnCustomers(): Resource<MutableList<Customer>> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()
        val customers = mutableListOf<Customer>()
        for (document in resultData){
            val custos  = document.get("customers")
            if(custos is List<*>){
                for (ref  in custos){
                    if(ref is DocumentReference){
                        val customerdoc = ref.get().await()
                        var customer = Customer()
                        customer.id = customerdoc.id
                        customer.name = customerdoc.getString("name")!!
                        customer.surname = customerdoc.getString("surname")!!
                        customer.photo = customerdoc.getString("photo")!!
                        customer.phone = customerdoc.getString("phone")!!
                        customer.email = customerdoc.getString("email")!!
                        customers.add(customer)
                    }
                }
            }

        }
        return Resource.Success(customers)
    }

    override suspend fun getCustomer(id: String): Resource<Customer> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .document(id)
            .get().await()
        var customer = resultData.toObject(Customer::class.java)
        customer!!.id = id
        return Resource.Success(customer)
    }

    override suspend fun createCustomer(customer: Customer): Resource<Boolean> {
        var goals_array = mutableListOf<HashMap<String, Any>>()
        for (goal in customer.goals){
            var hash_goal = hashMapOf<String, Any>("aim" to goal.aim, "isAchieved" to goal.isAchieved, "startDate" to goal.startDate, "endDate" to goal.endDate)
            goals_array.add(hash_goal)
        }
        var weights_array = mutableListOf<HashMap<String, Any>>()
        for (weight in customer.weights){
            var hash_weight = hashMapOf<String, Any>("date" to weight.date, "weight" to weight.weight)
            weights_array.add(hash_weight)
        }
        val data : HashMap<String, Any?> = hashMapOf("birthday" to customer.birthday, "dni" to customer.dni, "email" to customer.email, "name" to customer.name, "surname" to customer.surname, "goals" to goals_array, "photo" to customer.photo, "height" to customer.height, "phone" to customer.phone, "role" to customer.role, "weightPerWeek" to customer.weightPerWeek, "weights" to weights_array)

        val postResult = FirebaseFirestore.getInstance().collection("users").add(data).await()
        return Resource.Success(true)
    }
}