package com.tfg.workoutagent.models

import java.util.*

data class Day(
    var name:String = "DEFAULT_NAME",
    var workingDay : Date = Date(System.currentTimeMillis()),
    var completed : Boolean = false,
    var activities :MutableList<RoutineActivity> = mutableListOf()

                    ) {
}