package com.tfg.workoutagent.models

import java.util.*

data class Goal(val aim : String = "DEFAULT_AIM",
                val isAchieved : Boolean = false,
                val startDate : Date = Date(System.currentTimeMillis()),
                val endDate : Date = Date(System.currentTimeMillis() + 86400000)) {

}