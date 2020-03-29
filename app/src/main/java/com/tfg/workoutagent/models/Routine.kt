package com.tfg.workoutagent.models

import java.util.*

data class Routine(
    var id : String = "DEFAULT_ID",
    var startDate : Date = Date(System.currentTimeMillis()),
    var title : String = "DEFAULT_TITLE",
    var trainer: Trainer = Trainer(),
    var customer: Customer=Customer(),
    var days :MutableList<Day> = mutableListOf()
                    ) {
}