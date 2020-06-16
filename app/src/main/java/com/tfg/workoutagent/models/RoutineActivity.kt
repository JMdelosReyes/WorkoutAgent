package com.tfg.workoutagent.models

import java.util.*

data class RoutineActivity(
    var name:String = "DEFAULT_NAME",
    var exercise: Exercise = Exercise(),
    var note : String = "DEFAULT_NOTE",
    var repetitions :MutableList<Int> = mutableListOf(),
    var sets : Int = 3,
    var type : String = "DEFAULT_TYPE",
    var weightsPerRepetition :MutableList<Double> = mutableListOf()
                    ) {
}