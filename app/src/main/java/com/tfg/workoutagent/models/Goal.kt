package com.tfg.workoutagent.models

import com.google.firebase.firestore.PropertyName
import java.util.*

data class Goal(var aim : String = "DEFAULT_AIM",
                var description : String = "DEFAULT_DESCRIPTION",
                @set:PropertyName("isAchieved")
                @get:PropertyName("isAchieved")
                var isAchieved : Boolean = false,
                var startDate : Date = Date(System.currentTimeMillis()),
                var endDate : Date = Date(System.currentTimeMillis() + 86400000)) {
}