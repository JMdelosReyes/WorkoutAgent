package com.tfg.workoutagent.data.repositoriesImpl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Customer
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
}