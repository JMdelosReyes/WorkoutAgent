package com.tfg.workoutagent.models

import com.google.firebase.firestore.PropertyName
import java.util.*

data class RoutineActivity(
    var name:String = "DEFAULT_NAME",
    var exercise: Exercise = Exercise(),
    var note : String = "DEFAULT_NOTE",
    var repetitions :MutableList<Int> = mutableListOf(),
    var repetitionsCustomer :MutableList<Int> = mutableListOf(),
    var sets : Int = 0,
    var type : String = "DEFAULT_TYPE",
    var weightsPerRepetition :MutableList<Double> = mutableListOf(),
    var weightsPerRepetitionCustomer :MutableList<Double> = mutableListOf(),
    var completed : Boolean = false
                    ) {
}