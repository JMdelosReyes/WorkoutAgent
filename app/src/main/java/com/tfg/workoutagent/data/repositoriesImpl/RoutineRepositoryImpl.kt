package com.tfg.workoutagent.data.repositoriesImpl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tfg.workoutagent.data.repositories.RoutineRepository
import com.tfg.workoutagent.models.*
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class RoutineRepositoryImpl : RoutineRepository {

    override suspend fun getOwnRoutines(): Resource<MutableList<Routine>> {

        val trainerDB = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()

        val resultData = FirebaseFirestore.getInstance()
            .collection("routines")
            .whereEqualTo("trainer", trainerDB.documents[0].reference)
            .get().await()


        val routines = mutableListOf<Routine>()
        for (document in resultData) {
            val customerRef = document.get("customer")
            val trainerRef = document.get("trainer")
            var routine = Routine()
            routine.id = document.id
            routine.startDate = document.getTimestamp("startDate")!!.toDate()
            routine.title = document.getString("title")!!

            val days = document.get("days")

            if (days is HashMap<*, *>) {
                Log.i("Day", "${days.keys}")
                //iteramos por cada día
                for (dayKey in days.keys) {
                    var day = Day()
                    day.name = dayKey.toString()
                    var dia = days[dayKey]
                    if (dia is HashMap<*, *>) {
                        val diaAtributos = dia.keys
                        for (atributo in diaAtributos) {
                            when (atributo.toString()) {
                                "completed" -> day.completed = dia[atributo] as Boolean
                                "workingDay" -> {
                                    val tiempo = dia[atributo]
                                    if (tiempo is com.google.firebase.Timestamp) {
                                        day.workingDay = tiempo.toDate()
                                    }
                                }
                                "activities" -> {
                                    val actividades = dia[atributo]

                                    if (actividades is HashMap<*, *>) {
                                        for (actividad in actividades.keys) {
                                            var routineActivity = RoutineActivity()
                                            routineActivity.name = actividad.toString()
                                            val actividadesValues = actividades[actividad]

                                            if (actividadesValues is HashMap<*, *>) {
                                                for (activityAtributo in actividadesValues.keys) {
                                                    when (activityAtributo.toString()) {
                                                        "exercise" -> {
                                                            val docRefAct =
                                                                actividadesValues[activityAtributo]
                                                            if (docRefAct is DocumentReference) {
                                                                val exerciseDoc =
                                                                    docRefAct.get().await()
                                                                var exerciseAct = Exercise()
                                                                exerciseAct.id = exerciseDoc.id
                                                                exerciseAct.title =
                                                                    exerciseDoc.getString("title")!!
                                                                exerciseAct.description =
                                                                    exerciseDoc.getString("description")!!
                                                                exerciseAct.photos =
                                                                    (exerciseDoc.get("photos") as MutableList<String>?)!!
                                                                exerciseAct.tags =
                                                                    (exerciseDoc.get("tags") as MutableList<String>?)!!

                                                                routineActivity.exercise =
                                                                    exerciseAct
                                                            }
                                                        }
                                                        "note" -> routineActivity.note =
                                                            actividadesValues[activityAtributo].toString()
                                                        "repetitions" -> routineActivity.repetitions =
                                                            actividadesValues[activityAtributo] as MutableList<Int>
                                                        "repetitionsCustomer" -> routineActivity.repetitionsCustomer =
                                                            actividadesValues[activityAtributo] as MutableList<Int>
                                                        "sets" -> {
                                                            routineActivity.sets =
                                                                (actividadesValues[activityAtributo] as Long).toInt()
                                                        }
                                                        "type" -> routineActivity.type =
                                                            actividadesValues[activityAtributo].toString()
                                                        "weightsPerRepetition" -> routineActivity.weightsPerRepetition =
                                                            actividadesValues[activityAtributo] as MutableList<Double>
                                                        "weightsPerRepetitionCustomer" -> routineActivity.weightsPerRepetitionCustomer =
                                                            actividadesValues[activityAtributo] as MutableList<Double>

                                                    }
                                                }
                                            }
                                            day.activities.add(routineActivity)
                                        }

                                    }
                                }
                            }


                        }
                    }
                    //Log.i("Dia a añadir", "$day")
                    routine.days.add(day)
                }
            }

            if (customerRef is DocumentReference) {
                val customerDoc = customerRef.get().await()

                val customer = Customer()
                customer.id = customerDoc.id
                customer.name = customerDoc.getString("name")!!
                customer.surname = customerDoc.getString("surname")!!
                customer.photo = customerDoc.getString("photo")!!
                customer.phone = customerDoc.getString("phone")!!
                customer.birthday = customerDoc.getTimestamp("birthday")!!.toDate()
                customer.email = customerDoc.getString("email")!!
                customer.dni = customerDoc.getString("dni")!!
                routine.customer = customer
                Log.i("Customer", "$customer")
            }
            if (trainerRef is DocumentReference) {
                val trainerDoc = trainerRef.get().await()
                val trainer = Trainer()
                trainer.id = trainerDoc.id
                trainer.name = trainerDoc.getString("name")!!
                trainer.surname = trainerDoc.getString("surname")!!
                trainer.photo = trainerDoc.getString("photo")!!
                trainer.phone = trainerDoc.getString("phone")!!
                trainer.email = trainerDoc.getString("email")!!
                //val academicTitle:String = trainerDoc.get("academicTitle") as String
                /*if(academicTitle is HashMap<*,*>){
                    //No me siento orgulloso de esto
                    trainer.academicTitle = academicTitle["academicTitle"] as String
                }*/
                trainer.birthday = trainerDoc.getTimestamp("birthday")!!.toDate()
                trainer.dni = trainerDoc.getString("dni")!!
                //trainer.customers = (trainerDoc.get("customers") as MutableList<Customer>?)!!
                routine.trainer = trainer
            }
            Log.i("Dia a añadir", "$routine")
            routines.add(routine)
            //Log.i("RoutineList", routines.toString())

            //trainer.id = document.id
            //Log.i("UserRepository", "${trainer.id} ${trainer.academicTitle} ${trainer.birthday} ${trainer.customers} ${trainer.dni}" +
            //        "${trainer.email} ${trainer.name} ${trainer.email} ${trainer.phone}  ${trainer.photo} ${trainer.role} ${trainer.surname} ")
        }

        return Resource.Success(routines)
    }

    override suspend fun getRoutine(id: String): Resource<Routine> {

        val resultData =
            FirebaseFirestore.getInstance().collection("routines").document(id).get().await()

        val customerRef = resultData.get("customer")
        val trainerRef = resultData.get("trainer")
        var routine = Routine()
        routine.id = resultData.id
        routine.startDate = resultData.getTimestamp("startDate")!!.toDate()
        routine.title = resultData.getString("title")!!

        val days = resultData.get("days")
        if (days is ArrayList<*>) {
            //iteramos por cada día
            for (dayKey in days) {
                var day = Day()
                if (dayKey is HashMap<*, *>) {
                    val dayAttributes = dayKey.keys
                    for (attribute in dayAttributes) {
                        when (attribute.toString()) {
                            "completed" -> day.completed = dayKey[attribute] as Boolean
                            "name" -> day.name = dayKey[attribute].toString()
                            "workingDay" -> {
                                val time = dayKey[attribute]
                                if (time is com.google.firebase.Timestamp) {
                                    day.workingDay = time.toDate()
                                }
                            }
                            "activities" -> {
                                val activities = dayKey[attribute]

                                if (activities is ArrayList<*>) {
                                    for (activity in activities) {
                                        var routineActivity = RoutineActivity()

                                        if (activity is HashMap<*, *>) {
                                            for (activityAttribute in activity.keys) {
                                                when (activityAttribute.toString()) {
                                                    "exercise" -> {
                                                        val exerciseKey =
                                                            activity[activityAttribute]
                                                        if (exerciseKey is HashMap<*, *>) {
                                                            var exerciseAct = Exercise()
                                                            for (exerciseKeyAttribute in exerciseKey.keys) {
                                                                when (exerciseKeyAttribute.toString()) {
                                                                    "id" -> exerciseAct.id =
                                                                        exerciseKey[exerciseKeyAttribute].toString()
                                                                    "title" -> exerciseAct.title =
                                                                        exerciseKey[exerciseKeyAttribute].toString()
                                                                    "description" -> exerciseAct.description =
                                                                        exerciseKey[exerciseKeyAttribute].toString()
                                                                    "photos" -> exerciseAct.photos =
                                                                        exerciseKey[exerciseKeyAttribute] as MutableList<String>
                                                                    "tags" -> exerciseAct.tags =
                                                                        exerciseKey[exerciseKeyAttribute] as MutableList<String>
                                                                }
                                                            }
                                                            Log.i(
                                                                "Añadimos ejercicio",
                                                                "$exerciseAct"
                                                            )
                                                            routineActivity.exercise = exerciseAct
                                                        }
                                                    }
                                                    "note" -> routineActivity.note =
                                                        activity[activityAttribute].toString()
                                                    "name" -> routineActivity.name =
                                                        activity[activityAttribute].toString()
                                                    "repetitions" -> routineActivity.repetitions =
                                                        activity[activityAttribute] as MutableList<Int>
                                                    "repetitionsCustomer" -> routineActivity.repetitionsCustomer =
                                                        activity[activityAttribute] as MutableList<Int>
                                                    "sets" -> {
                                                        routineActivity.sets =
                                                            (activity[activityAttribute] as Long).toInt()
                                                    }
                                                    "type" -> routineActivity.type =
                                                        activity[activityAttribute].toString()
                                                    "weightsPerRepetition" -> routineActivity.weightsPerRepetition =
                                                        activity[activityAttribute] as MutableList<Double>
                                                    "weightsPerRepetitionCustomer" -> routineActivity.weightsPerRepetitionCustomer =
                                                        activity[activityAttribute] as MutableList<Double>
                                                    "completed" -> routineActivity.completed = activity[activityAttribute] as Boolean
                                                }
                                            }
                                        }
                                        day.activities.add(routineActivity)
                                    }

                                }
                            }
                        }


                    }
                }
                routine.days.add(day)
            }
        }

        if (customerRef is DocumentReference) {
            val customerDoc = customerRef.get().await()

            val customer = Customer()
            customer.id = customerDoc.id
            customer.name = customerDoc.getString("name")!!
            customer.surname = customerDoc.getString("surname")!!
            customer.photo = customerDoc.getString("photo")!!
            customer.phone = customerDoc.getString("phone")!!
            customer.birthday = customerDoc.getTimestamp("birthday")!!.toDate()
            customer.email = customerDoc.getString("email")!!
            customer.dni = customerDoc.getString("dni")!!
            routine.customer = customer
            Log.i("Customer", "$customer")
        }
        if (trainerRef is DocumentReference) {
            val trainerDoc = trainerRef.get().await()
            val trainer = Trainer()
            trainer.id = trainerDoc.id
            trainer.name = trainerDoc.getString("name")!!
            trainer.surname = trainerDoc.getString("surname")!!
            trainer.photo = trainerDoc.getString("photo")!!
            trainer.phone = trainerDoc.getString("phone")!!
            trainer.email = trainerDoc.getString("email")!!
            trainer.birthday = trainerDoc.getTimestamp("birthday")!!.toDate()
            trainer.dni = trainerDoc.getString("dni")!!
            routine.trainer = trainer
        }
        return Resource.Success(routine)
    }


    override suspend fun getActivityTimeline(): Resource<MutableList<TimelineActivity>> {
        val trainerDB = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()

        val resultData = FirebaseFirestore.getInstance()
            .collection("routines")
            .whereEqualTo("trainer", trainerDB.documents[0].reference)
            .get().await()

        val finishedActivities = mutableListOf<TimelineActivity>()

        for (document in resultData) {
            val timelineActivity = TimelineActivity()
            val customerRef = document.get("customer")
            val days = document.get("days")
            var dayList = mutableListOf<Day>()
            if (days is ArrayList<*>) {
                for (dayKey in days) {
                    var day = Day()
                    if (dayKey is HashMap<*, *>) {
                        val dayAttributes = dayKey.keys
                        for (attribute in dayAttributes) {
                            when (attribute.toString()) {
                                "completed" -> day.completed = dayKey[attribute] as Boolean
                                "workingDay" -> {
                                    val time = dayKey[attribute]
                                    if (time is com.google.firebase.Timestamp) {
                                        day.workingDay = time.toDate()
                                    }
                                }
                            }
                        }
                    }
                    Log.i("Probemos esta wea", "$day")
                    if (day.completed) {
                        dayList.add(day)
                    }
                }
            }
            dayList.sortBy { day -> day.workingDay }
            if (dayList.isNotEmpty() && customerRef != null) {
                timelineActivity.finishDate = dayList[dayList.size - 1].workingDay

                if (customerRef is DocumentReference) {
                    val customerDoc = customerRef.get().await()
                    timelineActivity.customerId = customerDoc.id
                    timelineActivity.customerPhoto = customerDoc.getString("photo")!!
                    timelineActivity.customerName =
                        customerDoc.getString("name")!! + " " + customerDoc.getString("surname")!!
                }
                finishedActivities.add(timelineActivity)
            }
        }
        return Resource.Success(finishedActivities)
    }

    override suspend fun createRoutine(routine: Routine): Resource<Boolean> {
        val data: HashMap<*, *>
        val trainerDB = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()

        if (routine.customer != null) {
            val customerDB = FirebaseFirestore.getInstance()
                .collection("users").document(routine.customer!!.id)
                .get().await()

            data = hashMapOf(
                "title" to routine.title,
                "startDate" to routine.startDate,
                "customer" to customerDB.reference,
                "trainer" to trainerDB.documents[0].reference,
                "days" to routine.days
            )
        } else {
            data = hashMapOf(
                "title" to routine.title,
                "startDate" to routine.startDate,
                "customer" to null,
                "trainer" to trainerDB.documents[0].reference,
                "days" to routine.days
            )
        }



        FirebaseFirestore.getInstance().collection("routines").add(data).await()
        return Resource.Success(true)
    }

    override suspend fun editRoutine(routine: Routine): Resource<Boolean> {
        val data: HashMap<String, Any?>
        val trainerDB = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()

        if (routine.customer != null) {
            val customerDB = FirebaseFirestore.getInstance()
                .collection("users").document(routine.customer!!.id)
                .get().await()

            data = hashMapOf(
                "title" to routine.title,
                "startDate" to routine.startDate,
                "customer" to customerDB.reference,
                "trainer" to trainerDB.documents[0].reference,
                "days" to routine.days
            )
        } else {
            data = hashMapOf(
                "title" to routine.title,
                "startDate" to routine.startDate,
                "customer" to null,
                "trainer" to trainerDB.documents[0].reference,
                "days" to routine.days
            )
        }


        FirebaseFirestore.getInstance().collection("routines").document(routine.id).update(data)
            .await()
        return Resource.Success(true)
    }

    override suspend fun deleteRoutine(id: String): Resource<Boolean> {
        FirebaseFirestore.getInstance().collection("routines").document(id).delete().await()
        return Resource.Success(true)
    }

    override suspend fun getAssignedRoutine(): Resource<Routine> {
        val customerRef = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0].reference
        val routine = FirebaseFirestore.getInstance()
            .collection("routines")
            .whereEqualTo("customer", customerRef)
            .orderBy("startDate", Query.Direction.DESCENDING)
            .limit(1)
            .get().await()
        return if (routine.documents.isEmpty()) {
            val routine = Routine()
            Resource.Success(routine)
        } else {
            val routineId = routine.documents[0].id
            this.getRoutine(routineId)
        }
    }

    override suspend fun getTodayActivities(): Resource<MutableList<RoutineActivity>> {
        return Resource.Success(mutableListOf())
    }

    override suspend fun getTemplateRoutines(): Resource<MutableList<Routine>> {
        val trainerDB = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await()

        val resultData = FirebaseFirestore.getInstance()
            .collection("routines")
            .whereEqualTo("trainer", trainerDB.documents[0].reference)
            .whereEqualTo("customer", null)
            .get().await()

        val routines = mutableListOf<Routine>()

        for (document in resultData) {
            routines.add(
                Routine(
                    id = document.id,
                    title = document.data["title"] as String
                )
            )
        }

        return Resource.Success(routines)
    }

    override suspend fun updateDay(updatedDay: Day): Resource<Boolean> {
        val assignedRoutine = this.getAssignedRoutine()
        if(assignedRoutine is Resource.Success){
            val routine = assignedRoutine.data
            var index = 0
            for (day in routine.days){
                if(!day.completed) {
                    routine.days[index] = updatedDay
                    break
                }
                index++
            }
            val res = this.editRoutine(routine)
            return if(res is Resource.Success) Resource.Success(true) else res
        }

        return Resource.Failure(Exception("ERROR"))
    }
}