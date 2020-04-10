package com.tfg.workoutagent.data.repositoriesImpl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.workoutagent.data.repositories.RoutineRepository
import com.tfg.workoutagent.models.*
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.HashMap

class RoutineRepositoryImpl: RoutineRepository {

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
        for (document in resultData){
            val customerRef  = document.get("customer")
            val trainerRef  = document.get("trainer")
            var routine = Routine()
            routine.id = document.id
            routine.startDate = document.getTimestamp("startDate")!!.toDate()
            routine.title = document.getString("title")!!

            val days = document.get("days")

            if(days is HashMap<*,*>){
                Log.i("Day", "${days.keys}")
                //iteramos por cada día
                for(dayKey in days.keys){
                    var day = Day()
                    day.name = dayKey.toString()
                    var dia = days[dayKey]
                    if(dia is HashMap<*,*>){
                        val diaAtributos = dia.keys
                        for(atributo in diaAtributos){
                            when(atributo.toString()) {
                                "completed" ->   day.completed = dia[atributo] as Boolean
                                "workingDay" -> {
                                    val tiempo = dia[atributo]
                                    if(tiempo is com.google.firebase.Timestamp){
                                        day.workingDay = tiempo.toDate()
                                    }
                                }
                                "activities" ->  {
                                    val actividades =  dia[atributo]

                                    if(actividades is HashMap<*,*>){
                                        for(actividad in actividades.keys){
                                            var routineActivity = RoutineActivity()
                                            routineActivity.name = actividad.toString()
                                            val actividadesValues = actividades[actividad]

                                            if(actividadesValues is HashMap<*,*>){
                                                for(activityAtributo in actividadesValues.keys){
                                                    when(activityAtributo.toString()) {
                                                        "exercise" -> {
                                                            val docRefAct = actividadesValues[activityAtributo]
                                                            if(docRefAct is DocumentReference){
                                                                val exerciseDoc = docRefAct.get().await()
                                                                var exerciseAct = Exercise()
                                                                exerciseAct.id = exerciseDoc.id
                                                                exerciseAct.title = exerciseDoc.getString("title")!!
                                                                exerciseAct.description = exerciseDoc.getString("description")!!
                                                                exerciseAct.photos = (exerciseDoc.get("photos") as MutableList<String>?)!!
                                                                exerciseAct.tags = (exerciseDoc.get("tags") as MutableList<String>?)!!

                                                                routineActivity.exercise = exerciseAct
                                                            }
                                                        }
                                                        "note"-> routineActivity.note = actividadesValues[activityAtributo].toString()
                                                        "repetitions" -> routineActivity.repetitions =
                                                            actividadesValues[activityAtributo] as MutableList<Int>
                                                        "set" -> routineActivity.sets =
                                                            actividadesValues[activityAtributo] as Int
                                                        "type" -> routineActivity.type = actividadesValues[activityAtributo].toString()
                                                        "weightsPerRepetition" -> routineActivity.weightsPerRepetition =
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

            if(customerRef is DocumentReference){
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
            if(trainerRef is DocumentReference){
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

        //val trainer : Trainer = resultData.documents[0].toObject(Trainer::class.java)!!
        //Log.i("REPO USUARIOS", "${trainer.name}")
        return Resource.Success(routines)
    }

    override suspend fun getRoutine(id: String): Resource<Routine> {

        val resultData = FirebaseFirestore.getInstance().collection("routines").document(id).get().await()

        val customerRef  = resultData.get("customer")
        val trainerRef  = resultData.get("trainer")
        var routine = Routine()
        routine!!.id = resultData.id
        routine!!.startDate = resultData.getTimestamp("startDate")!!.toDate()
        routine!!.title = resultData.getString("title")!!

        val days = resultData.get("days")

        if(days is HashMap<*,*>){
            //iteramos por cada día
            for(dayKey in days.keys){
                var day = Day()
                day.name = dayKey.toString()
                var dayKey = days[dayKey]
                if(dayKey is HashMap<*,*>){
                    val dayAtributes = dayKey.keys
                    for(atribute in dayAtributes){
                        when(atribute.toString()) {
                            "completed" ->   day.completed = dayKey[atribute] as Boolean
                            "workingDay" -> {
                                val time = dayKey[atribute]
                                if(time is com.google.firebase.Timestamp){
                                    day.workingDay = time.toDate()
                                }
                            }
                            "activities" ->  {
                                val activities =  dayKey[atribute]

                                if(activities is HashMap<*,*>){
                                    for(activity in activities.keys){
                                        var routineActivity = RoutineActivity()
                                        routineActivity.name = activity.toString()
                                        val activitiesValues = activities[activity]

                                        if(activitiesValues is HashMap<*,*>){
                                            for(activityAtribute in activitiesValues.keys){
                                                when(activityAtribute.toString()) {
                                                    "exercise" -> {
                                                        val docRefAct = activitiesValues[activityAtribute]
                                                        if(docRefAct is DocumentReference){
                                                            val exerciseDoc = docRefAct.get().await()
                                                            var exerciseAct = Exercise()
                                                            exerciseAct.id = exerciseDoc.id
                                                            exerciseAct.title = exerciseDoc.getString("title")!!
                                                            exerciseAct.description = exerciseDoc.getString("description")!!
                                                            exerciseAct.photos = (exerciseDoc.get("photos") as MutableList<String>?)!!
                                                            exerciseAct.tags = (exerciseDoc.get("tags") as MutableList<String>?)!!

                                                            routineActivity.exercise = exerciseAct
                                                        }
                                                    }
                                                    "note"-> routineActivity.note = activitiesValues[activityAtribute].toString()
                                                    "repetitions" -> routineActivity.repetitions =
                                                        activitiesValues[activityAtribute] as MutableList<Int>
                                                    "set" -> routineActivity.sets =
                                                        activitiesValues[activityAtribute] as Int
                                                    "type" -> routineActivity.type = activitiesValues[activityAtribute].toString()
                                                    "weightsPerRepetition" -> routineActivity.weightsPerRepetition =
                                                        activitiesValues[activityAtribute] as MutableList<Double>

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
                Log.i("Dia a añadir", "$day")
                routine.days.add(day)
            }
        }

        if(customerRef is DocumentReference){
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
        if(trainerRef is DocumentReference){
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

        for (document in resultData){
            val timelineActivity = TimelineActivity()
            val customerRef  = document.get("customer")
            val days = document.get("days")
            var dayList = mutableListOf<Day>()
            if(days is HashMap<*,*>){
                for(dayKey in days.keys) {
                    var day = Day()
                    var dayKey = days[dayKey]
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
                    if(day.completed){
                        dayList.add(day)
                    }
                }
            }
            dayList.sortBy { day -> day.workingDay }
            timelineActivity.finishDate = dayList[dayList.size -1].workingDay
            if(customerRef is DocumentReference){
                val customerDoc = customerRef.get().await()
                timelineActivity.customerId =customerDoc.id
                timelineActivity.customerPhoto = customerDoc.getString("photo")!!
                timelineActivity.customerName = customerDoc.getString("name")!! + " " + customerDoc.getString("surname")!!
            }
            finishedActivities.add(timelineActivity)
        }
        return Resource.Success(finishedActivities)
    }

    override suspend fun createRoutine(routine: Routine): Resource<Boolean> {

        val data = hashMapOf(
            "title" to routine.title,
            "startDate" to routine.startDate,
            "customer" to routine.customer,
            "trainer" to routine.trainer,
            "days" to routine.days
        )
        FirebaseFirestore.getInstance().collection("routines").add(data).await()
        return Resource.Success(true)
    }

    override suspend fun editRoutine(routine: Routine): Resource<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteRoutine(id: String): Resource<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}