package com.tfg.workoutagent.vo.utils

import java.text.SimpleDateFormat
import java.util.*

fun parseDateToFriendlyDate(date: Date) : String? {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    return sdf.format(date)
}

fun parseStringToDate(string: String?) : Date? {
    val pattern = "dd-MM-yyyy"
    val sdf = SimpleDateFormat(pattern)
    return if (string != null && string != "" && string != "dd-MM-yyyy") {
        sdf.parse(string)
    } else {
        val nullDate : Date? = null
        nullDate
    }
}


fun getAgeWithError(it : String) : String {
    var error = ""
    val cal = Calendar.getInstance()
    val arr  = it.split("-")
    val dayBirth = arr[0].toInt()
    val monthBirth = arr[1].toInt()
    val yearBirth = arr[2].toInt()
    var years = cal.get(Calendar.YEAR) - yearBirth
    if(monthBirth < cal.get(Calendar.MONTH) + 1|| monthBirth==cal.get(Calendar.MONTH)+1 && dayBirth <= cal.get(Calendar.DAY_OF_MONTH)){
        years++
    }
    if(years < 18){
        error = "The age of new user must be over 18 years old"
    }
    return error
}

fun getAge(it : String) : Int {
    var years = 0
    if(it != ""){
        val cal = Calendar.getInstance()
        val arr  = it.split("-")
        val dayBirth = arr[0].toInt()
        val monthBirth = arr[1].toInt()
        val yearBirth = arr[2].toInt()
        years = cal.get(Calendar.YEAR) - yearBirth
        if(monthBirth < cal.get(Calendar.MONTH) + 1|| monthBirth==cal.get(Calendar.MONTH)+1 && dayBirth <= cal.get(Calendar.DAY_OF_MONTH)){
            years++
        }
    }

    return years
}