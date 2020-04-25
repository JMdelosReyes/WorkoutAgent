package com.tfg.workoutagent.data.repositoriesImpl

import android.util.Log
import android.content.Intent
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.storage.FirebaseStorage
import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository {

    override suspend fun updateProfileAdmin(): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfileCustomer(): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfileTrainer(): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    //TRAINER
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
                        if(customerdoc.getString("photo") != null) customer.photo = customerdoc.getString("photo")!! else customer.photo = ""
                        Log.i("customer.photo", "1 ${customer.photo}")
                        customer.phone = customerdoc.getString("phone")!!
                        customer.email = customerdoc.getString("email")!!
                        Log.i("customer repo", customer.toString())
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
        val customer = resultData.toObject(Customer::class.java)
        customer!!.id = id
        return Resource.Success(customer)
    }

    override suspend fun createCustomer(customer: Customer): Resource<Boolean> {
        val goalsArray = mutableListOf<HashMap<String, Any>>()
        for (goal in customer.goals){
            val hashGoal = hashMapOf<String, Any>("aim" to goal.aim, "isAchieved" to goal.isAchieved, "startDate" to goal.startDate, "endDate" to goal.endDate)
            goalsArray.add(hashGoal)
        }
        val weightsArray = mutableListOf<HashMap<String, Any>>()
        for (weight in customer.weights){
            val hashWeight = hashMapOf<String, Any>("date" to weight.date, "weight" to weight.weight)
            weightsArray.add(hashWeight)
        }
        val data : HashMap<String, Any?> = hashMapOf("birthday" to customer.birthday, "dni" to customer.dni, "email" to customer.email, "name" to customer.name, "surname" to customer.surname, "goals" to goalsArray, "photo" to customer.photo, "height" to customer.height, "phone" to customer.phone, "role" to customer.role, "weightPerWeek" to customer.weightPerWeek, "weights" to weightsArray)
        //POST del nuevo customer
        val postResult = FirebaseFirestore.getInstance().collection("users").add(data).await()
        //Obtengo el trainer logeado para incluirle un nuevo customer en su listado
        val trainerLoggedId = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0].id
        //Actualizo el trainer logeado
        FirebaseFirestore.getInstance().collection("users").document(trainerLoggedId).update("customers", FieldValue.arrayUnion(postResult!!)).await()
        return Resource.Success(true)
    }

    override suspend fun createTrainer(trainer: Trainer): Resource<Boolean> {
        val customers = mutableListOf<DocumentReference>()
        val data : HashMap<String, Any> = hashMapOf("birthday" to trainer.birthday, "customers" to customers, "dni" to trainer.dni, "email" to trainer.email, "name" to trainer.name, "surname" to trainer.surname, "phone" to trainer.phone, "photo" to trainer.photo, "role" to trainer.role)
        val postResult = FirebaseFirestore.getInstance().collection("users").add(data).await()
        return Resource.Success(true)
    }

    override suspend fun getLoggedUserCustomer(): Resource<Customer> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()
        val customer = resultData.documents[0].toObject(Customer::class.java)!!
        customer.id = resultData.documents[0].id
        return Resource.Success(customer)
    }

    override suspend fun getLoggedUserTrainer(): Resource<Trainer> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0]
        var trainer = Trainer(id = resultData.id,
            name = resultData.getString("name")!!,
            surname = resultData.getString("surname")!!,
            email = resultData.getString("email")!!,
            dni = resultData.getString("dni")!!,
            phone = resultData.getString("phone")!!,
            photo = resultData.getString("photo")!!,
            birthday = resultData.getDate("birthday")!!
            )
        return Resource.Success(trainer)
    }

    override suspend fun updateCustomer(customer: Customer): Resource<Boolean> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .document(customer.id)
            .get().await().toObject(Customer::class.java)!!
        //Para no perder los goals anteriores del usuario, los extraemos de base de datos
        val goalsArray = mutableListOf<HashMap<String, Any>>()
        for (goal in resultData.goals){
            val hashGoal = hashMapOf<String, Any>("aim" to goal.aim, "isAchieved" to goal.isAchieved, "startDate" to goal.startDate, "endDate" to goal.endDate)
            goalsArray.add(hashGoal)
        }
        //Para actualizar el ultimo peso del usuario, tomo el array completo de base de datos para no perder el resto de información
        val prevWeights = resultData.weights
        val weightsArray = mutableListOf<HashMap<String, Any>>()
        for (weight in prevWeights){
            val hashWeight = hashMapOf<String, Any>("date" to weight.date, "weight" to weight.weight)
            weightsArray.add(hashWeight)
        }
        //TODO: Modificar para no perder la foto subida en Firestore
        if(customer.photo == "" || customer.photo == "DEFAULT_PHOTO"){
            customer.photo = resultData.photo
        }
        val data : HashMap<String, Any?> = hashMapOf("birthday" to customer.birthday, "dni" to customer.dni, "email" to customer.email, "name" to customer.name, "surname" to customer.surname, "goals" to goalsArray, "photo" to customer.photo, "height" to customer.height, "phone" to customer.phone, "role" to customer.role, "weightPerWeek" to customer.weightPerWeek, "weights" to weightsArray,
            "formula" to customer.formula, "formulaType" to customer.formulaType)
        FirebaseFirestore.getInstance().collection("users").document(customer.id).update(data).await()
        return Resource.Success(true)
    }

    override suspend fun canDeleteCustomer(id: String): Resource<Boolean> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()
        var canDelete = false
        for (document in resultData) {
            val custos = document.get("customers")
            if (custos is List<*>) {
                for (ref in custos) {
                    if (ref is DocumentReference) {
                        val customerdoc = ref.get().await()
                        if(customerdoc.id == id){
                            canDelete = true
                            return Resource.Success(canDelete)
                        }
                    }
                }
            }
        }
        return Resource.Success(canDelete)
    }

    override suspend fun deleteCustomer(id: String): Resource<Boolean> {
        val currentTrainerId = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0].id
        val customerToBeDeleted = FirebaseFirestore.getInstance().collection("users").document(id)
        FirebaseFirestore.getInstance().collection("users").document(currentTrainerId).update("customers", FieldValue.arrayRemove(customerToBeDeleted)).await()
        FirebaseFirestore.getInstance().collection("users").document(id).delete().await()
        return Resource.Success(true)
    }


    //ADMIN

    override suspend fun getCustomersAdmin(): Resource<MutableList<Customer>> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("role", "CUSTOMER")
            .get().await().toObjects(Customer::class.java)
        return Resource.Success(resultData)
    }

    override suspend fun getTrainersAdmin(): Resource<MutableList<Trainer>> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("role", "TRAINER")
            .get().await()
        val trainers = mutableListOf<Trainer>()
        for (document in resultData.documents){
            val trainer = Trainer(id = document.id, name = document.getString("name")!!, surname = document.getString("surname")!!, photo = document.getString("photo")!!, email = document.getString("email")!!, phone = document.getString("phone")!!)
            trainers.add(trainer)
        }
        return Resource.Success(trainers)
    }
}