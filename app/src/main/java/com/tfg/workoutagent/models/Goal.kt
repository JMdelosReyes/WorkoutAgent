package com.tfg.workoutagent.models

import java.util.*

data class Goal(var aim : String = "DEFAULT_AIM",
                var isAchieved : Boolean = false,
                var startDate : Date = Date(System.currentTimeMillis()),
                var endDate : Date = Date(System.currentTimeMillis() + 86400000)) {
}