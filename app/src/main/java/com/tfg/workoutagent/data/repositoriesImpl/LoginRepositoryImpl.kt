package com.tfg.workoutagent.data.repositoriesImpl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.tfg.workoutagent.data.repositories.LoginRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class LoginRepositoryImpl : LoginRepository {
    override suspend fun getRole(): Resource<String> {
        val result = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .limit(1)
            .get()
            .await()
        if (result.documents[0] != null) {
            return Resource.Success(result.documents[0].getString("role")!!)
        } else {
            return Resource.Success("NO_ACCOUNT")
        }
    }

    override suspend fun updateToken(): Resource<String> {
        var token = ""
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            it.result?.token?.let { it ->
                token = it
            }
        }
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0]

        val data: HashMap<String, Any?> = hashMapOf("token" to token)
        FirebaseFirestore.getInstance().collection("users").document(resultData.id).update(data)
            .await()
        return Resource.Success(token)
    }

    override suspend fun getTrainerByCustomerId(): Resource<Trainer> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("role", "TRAINER")
            .get().await()
        val resultDataCustomer = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()
        val customer = resultDataCustomer.documents[0].toObject(Customer::class.java)!!
        customer.id = resultDataCustomer.documents[0].id
        var trainer: Trainer? = null
        stop@ for (document in resultData.documents) {
            val custos = document.get("customers")
            if (custos is List<*>) {
                for (ref in custos) {
                    if (ref is DocumentReference) {
                        val customerdoc = ref.get().await()
                        if (customerdoc.id == customer.id) {
                            trainer = Trainer(
                                id = document.id,
                                name = document.getString("name")!!,
                                surname = document.getString("surname")!!,
                                email = document.getString("email")!!,
                                dni = document.getString("dni")!!,
                                phone = document.getString("phone")!!,
                                photo = document.getString("photo")!!,
                                birthday = document.getDate("birthday")!!,
                                academicTitle = document.getString("academicTitle")!!
                            )
                            break@stop
                            //return Resource.Success(trainer)
                        }
                    }
                }
            }
        }
        return if (trainer != null) {
            Resource.Success(trainer)
        } else {
            Resource.Failure(Exception("There's no trainer associated to you"))
        }
    }

    override suspend fun getIdTrainerByCustomerId(): Resource<String> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("role", "TRAINER")
            .get().await()
        val resultDataCustomer = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()
        val customer = resultDataCustomer.documents[0].toObject(Customer::class.java)!!
        customer.id = resultDataCustomer.documents[0].id
        var trainerId: String = ""
        stop@ for (document in resultData.documents) {
            val custos = document.get("customers")
            if (custos is List<*>) {
                for (ref in custos) {
                    if (ref is DocumentReference) {
                        val customerdoc = ref.get().await()
                        if (customerdoc.id == customer.id) {
                            trainerId = document.id
                            break@stop
                        }
                    }
                }
            }
        }
        return if (trainerId != "") {
            Resource.Success(trainerId)
        } else {
            Resource.Failure(Exception("There's no trainer associated to you"))
        }
    }


    override suspend fun getLoggedUserTrainer(): Resource<Trainer> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0]
        val trainer = Trainer(id = resultData.id,
            name = resultData.getString("name")!!,
            surname = resultData.getString("surname")!!,
            email = resultData.getString("email")!!,
            dni = resultData.getString("dni")!!,
            phone = resultData.getString("phone")!!,
            photo = resultData.getString("photo")!!,
            birthday = resultData.getDate("birthday")!!,
            academicTitle = resultData.getString("academicTitle")!!
        )
        return Resource.Success(trainer)
    }
}