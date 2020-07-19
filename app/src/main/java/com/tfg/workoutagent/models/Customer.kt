package com.tfg.workoutagent.models

import com.google.firebase.firestore.PropertyName
import java.util.*

data class Customer(
    var id : String = "DEFAULT_ID",
    var birthday : Date = Date(System.currentTimeMillis()),
    var dni : String = "DEFAULT_DNI",
    var email : String = "DEFAULT_EMAIL",
    var name : String = "DEFAULT_NAME",
    var surname : String = "DEFAULT_SURNAME",
    @set:PropertyName("goals")
    @get:PropertyName("goals")
    var goals : MutableList<Goal> = mutableListOf(),
    var photo : String = "DEFAULT_PHOTO",
    var height : Int = 150,
    var phone : String = "DEFAULT_PHONE",
    var role : String = "CUSTOMER",
    var weightPerWeek : Double = 1.0,
    var weights : MutableList<Weight> = mutableListOf(),
    var formula : String = "Harris-Benedict",
    var token: String = "TOKEN",
    var formulaType : String = "Harris-Benedict",
    var genre: String = "M"
                    ) {
}