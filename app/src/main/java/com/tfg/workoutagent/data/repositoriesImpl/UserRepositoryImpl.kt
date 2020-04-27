package com.tfg.workoutagent.data.repositoriesImpl

import android.util.Log
import android.content.Intent
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
//import com.google.firebase.storage.FirebaseStorage
import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Administrator
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository {

    //CUSTOMER
    override suspend fun getCustomer(id: String): Resource<Customer> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .document(id)
            .get().await()
        val customer = resultData.toObject(Customer::class.java)
        customer!!.id = id
        Log.i("customer", customer.toString())
        return Resource.Success(customer)
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

    override suspend fun getCustomersAdmin(): Resource<MutableList<Customer>> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("role", "CUSTOMER")
            .get().await()
        val customers = mutableListOf<Customer>()
        for (document in resultData.documents){
            val customer = Customer(id = document.id, name = document.getString("name")!!, surname = document.getString("surname")!!, photo = document.getString("photo")!!, email = document.getString("email")!!, phone = document.getString("phone")!!)
            customers.add(customer)
        }
        return Resource.Success(customers)
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
        val data : HashMap<String, Any?> = hashMapOf("birthday" to customer.birthday, "dni" to customer.dni, "genre" to customer.genre, "email" to customer.email, "name" to customer.name, "surname" to customer.surname, "goals" to goalsArray, "photo" to customer.photo, "height" to customer.height, "phone" to customer.phone, "role" to "CUSTOMER", "weightPerWeek" to customer.weightPerWeek, "weights" to weightsArray)
        val postResult = FirebaseFirestore.getInstance().collection("users").add(data).await()
        val trainerLoggedId = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0].id
        FirebaseFirestore.getInstance().collection("users").document(trainerLoggedId).update("customers", FieldValue.arrayUnion(postResult!!)).await()
        return Resource.Success(true)
    }

    override suspend fun updateCustomer(customer: Customer): Resource<Boolean> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .document(customer.id)
            .get().await().toObject(Customer::class.java)!!
        val goalsArray = mutableListOf<HashMap<String, Any>>()
        for (goal in resultData.goals){
            val hashGoal = hashMapOf<String, Any>("aim" to goal.aim, "isAchieved" to goal.isAchieved, "startDate" to goal.startDate, "endDate" to goal.endDate)
            goalsArray.add(hashGoal)
        }
        val prevWeights = resultData.weights
        val weightsArray = mutableListOf<HashMap<String, Any>>()
        for (weight in prevWeights){
            val hashWeight = hashMapOf<String, Any>("date" to weight.date, "weight" to weight.weight)
            weightsArray.add(hashWeight)
        }
        if(customer.photo == "" || customer.photo == "DEFAULT_PHOTO"){
            customer.photo = resultData.photo
        }
        val data : HashMap<String, Any?> = hashMapOf("birthday" to customer.birthday, "dni" to customer.dni,"genre" to resultData.genre, "email" to customer.email, "name" to customer.name, "surname" to customer.surname, "goals" to goalsArray, "photo" to customer.photo, "height" to customer.height, "phone" to customer.phone, "role" to customer.role, "weightPerWeek" to customer.weightPerWeek, "weights" to weightsArray,
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
        val customerQuery = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()

        val currentTrainerId = customerQuery.documents[0].id

        val resultData = FirebaseFirestore.getInstance()
            .collection("routines")
            .whereEqualTo("customer", customerQuery.documents[0].reference)
            .get().await()
        for (document in resultData){
            FirebaseFirestore.getInstance().collection("routines").document(currentTrainerId).update("customer", null)
        }

        val customerToBeDeleted = FirebaseFirestore.getInstance().collection("users").document(id)
        FirebaseFirestore.getInstance().collection("users").document(currentTrainerId).update("customers", FieldValue.arrayRemove(customerToBeDeleted)).await()
        FirebaseFirestore.getInstance().collection("users").document(id).delete().await()
        return Resource.Success(true)
    }

    override suspend fun deleteLoggedCustomer(): Resource<Boolean> {
        val customerDB = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()

        val id = FirebaseFirestore.getInstance()
           .collection("users")
           .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
           .get().await().documents[0].id

        val resultData = FirebaseFirestore.getInstance()
          .collection("routines")
          .whereEqualTo("customer", customerDB.documents[0].reference)
          .get().await()

        for (document in resultData){
            FirebaseFirestore.getInstance().collection("routines").document(document.id).update("customer", null)
        }
        FirebaseFirestore.getInstance().collection("users").document(id).delete().await()
        return Resource.Success(true)
    }

    override suspend fun deleteCustomerAdmin(id: String): Resource<Boolean> {
        val customerToBeDeleted = FirebaseFirestore.getInstance().collection("users").document(id)

        val resultData = FirebaseFirestore.getInstance()
            .collection("routines")
            .whereEqualTo("customer", customerToBeDeleted)
            .get().await()
        for (document in resultData){
            FirebaseFirestore.getInstance().collection("routines").document(document.id).update("customer", null)
        }
        val searchUser = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("role","TRAINER")
            .whereArrayContains("customers", customerToBeDeleted).get().await().documents[0].id

        FirebaseFirestore.getInstance().collection("users").document(searchUser).update("customers", FieldValue.arrayRemove(customerToBeDeleted)).await()
        FirebaseFirestore.getInstance().collection("users").document(id).delete().await()
        return Resource.Success(true)
    }

    override suspend fun updateProfileCustomer(customer: Customer): Resource<Boolean> {
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
        val prevWeights = resultData.weights
        val weightsArray = mutableListOf<HashMap<String, Any>>()
        for (weight in prevWeights){
            val hashWeight = hashMapOf<String, Any>("date" to weight.date, "weight" to weight.weight)
            weightsArray.add(hashWeight)
        }
        if(customer.photo == "" || customer.photo == "DEFAULT_PHOTO"){
            customer.photo = resultData.photo
        }
        val data : HashMap<String, Any?> = hashMapOf("birthday" to customer.birthday, "dni" to customer.dni, "email" to customer.email, "name" to customer.name, "surname" to customer.surname, "goals" to goalsArray, "photo" to customer.photo, "height" to customer.height, "phone" to customer.phone, "role" to "CUSTOMER", "weightPerWeek" to customer.weightPerWeek, "weights" to weightsArray)
        FirebaseFirestore.getInstance().collection("users").document(customer.id).update(data).await()

        return Resource.Success(true)
    }


    //TRAINER

    override suspend fun getTrainer(id: String): Resource<Trainer> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .document(id)
            .get().await()
        val trainer = Trainer(id = resultData.id,
            name = resultData.getString("name")!!,
            surname = resultData.getString("surname")!!,
            email = resultData.getString("email")!!,
            dni = resultData.getString("dni")!!,
            phone = resultData.getString("phone")!!,
            photo = resultData.getString("photo")!!,
            birthday = resultData.getDate("birthday")!!,
            academicTitle = resultData.getString("academicTitle")!!)
        return Resource.Success(trainer)
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

    override suspend fun createTrainer(trainer: Trainer): Resource<Boolean> {
        val customers = mutableListOf<DocumentReference>()
        val data : HashMap<String, Any> = hashMapOf("birthday" to trainer.birthday, "customers" to customers, "dni" to trainer.dni, "email" to trainer.email, "name" to trainer.name, "surname" to trainer.surname, "phone" to trainer.phone, "photo" to trainer.photo, "role" to trainer.role)
        val postResult = FirebaseFirestore.getInstance().collection("users").add(data).await()
        return Resource.Success(true)
    }

    override suspend fun updateTrainer(trainer: Trainer): Resource<Boolean> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .document(trainer.id)
            .get().await()

        val customers = mutableListOf<DocumentReference>()
        val dataCustomers = resultData.get("customers")
        if(dataCustomers is List<*>){
            for (custo in dataCustomers){
                if(custo is DocumentReference){
                    customers.add(custo)
                }
            }
        }

        val trainerFB = Trainer(id = resultData.id,
            name = resultData.getString("name")!!,
            surname = resultData.getString("surname")!!,
            email = resultData.getString("email")!!,
            dni = resultData.getString("dni")!!,
            phone = resultData.getString("phone")!!,
            photo = resultData.getString("photo")!!,
            birthday = resultData.getDate("birthday")!!,
            academicTitle = resultData.getString("academicTitle")!!
        )

        if(trainer.photo != "" && trainer.photo != "DEFAULT_PHOTO"){
            trainerFB.photo = trainer.photo
        }
        if(trainer.academicTitle != "" && trainer.academicTitle != "DEFAULT_ACADEMIC_TITLE"){
            trainerFB.academicTitle = trainer.academicTitle
        }
        val data : HashMap<String, Any?> = hashMapOf("birthday" to trainerFB.birthday, "dni" to trainerFB.dni, "email" to trainerFB.email, "name" to trainerFB.name, "surname" to trainerFB.surname, "photo" to trainerFB.photo,  "phone" to trainerFB.phone, "role" to trainerFB.role,  "customers" to customers)
        FirebaseFirestore.getInstance().collection("users").document(trainer.id).update(data).await()
        return Resource.Success(true)
    }

    override suspend fun deleteTrainer(id: String): Resource<Boolean> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .document(id)
            .get().await()

        FirebaseFirestore.getInstance().collection("users").document(id).delete().await()
        return Resource.Success(true)
    }

    override suspend fun deleteLoggedTrainer(): Resource<Boolean> {
        val trainerDB = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()
        val id = trainerDB.documents[0].id
        val resultData = FirebaseFirestore.getInstance()
            .collection("routines")
            .whereEqualTo("trainer", trainerDB.documents[0].reference)
            .get().await()
        for (document in resultData){
            FirebaseFirestore.getInstance().collection("routines").document(document.id).update("trainer", null)
        }
        FirebaseFirestore.getInstance().collection("users").document(id).delete().await()
        return Resource.Success(true)
    }

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
                        val customer = Customer()
                        customer.id = customerdoc.id
                        customer.name = customerdoc.getString("name")!!
                        customer.surname = customerdoc.getString("surname")!!
                        if(customerdoc.getString("photo") != null) customer.photo = customerdoc.getString("photo")!! else customer.photo = ""
                        customer.phone = customerdoc.getString("phone")!!
                        customer.email = customerdoc.getString("email")!!
                        customers.add(customer)
                    }
                }
            }

        }
        return Resource.Success(customers)
    }

    override suspend fun updateProfileTrainer(trainerEdited: Trainer): Resource<Boolean> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0]

        val customers = mutableListOf<DocumentReference>()
        val dataCustomers = resultData.get("customers")
        if(dataCustomers is List<*>){
            for (custo in dataCustomers){
                if(custo is DocumentReference){
                    customers.add(custo)
                }
            }
        }
        Log.i("TRAINER PARAM", trainerEdited.toString())
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
        Log.i("TRAINER FIREBASE", trainer.toString())

        if(trainerEdited.photo != "" && trainerEdited.photo != "DEFAULT_PHOTO"){
            trainer.photo = trainerEdited.photo
        }
        if(trainerEdited.academicTitle != "" && trainerEdited.academicTitle != "DEFAULT_ACADEMIC_TITLE"){
            trainer.academicTitle = trainerEdited.academicTitle
        }
        val data : HashMap<String, Any?> = hashMapOf("birthday" to trainerEdited.birthday, "dni" to trainerEdited.dni, "email" to trainerEdited.email, "name" to trainerEdited.name, "surname" to trainerEdited.surname, "photo" to trainer.photo,  "phone" to trainerEdited.phone, "role" to trainer.role,  "customers" to customers, "academicTitle" to trainer.academicTitle)
        FirebaseFirestore.getInstance().collection("users").document(trainer.id).update(data).await()
        return Resource.Success(true)
    }

    //ADMIN

    override suspend fun updateProfileAdmin(admin: Administrator): Resource<Boolean> {
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0]
        admin.id = resultData.id
        if(admin.photo != "" && admin.photo != "DEFAULT_PHOTO"){
            admin.photo = resultData.getString("photo")!!
        }
        val data : HashMap<String, Any?> = hashMapOf("birthday" to admin.birthday, "dni" to admin.dni, "email" to admin.email, "name" to admin.name, "surname" to admin.surname, "photo" to admin.photo,  "phone" to admin.phone, "role" to admin.role)
        FirebaseFirestore.getInstance().collection("users").document(admin.id).update(data).await()
        return Resource.Success(true)
    }

}