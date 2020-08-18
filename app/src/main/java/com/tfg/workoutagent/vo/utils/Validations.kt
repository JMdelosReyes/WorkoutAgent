package com.tfg.workoutagent.vo.utils

import java.util.*


fun checkBirthday(birthday: String?): String {
    var error = ""
    if(birthday != null && birthday.contains("-")){
        val date = parseStringToDate(birthday)

        birthday.let {
            if (date == null) error = "Birthday cannot be null" else error =
                getAgeWithError(it)
        }
    }else{
        val date = parseStringToDateBar(birthday)

        birthday.let {
            if (date == null) error = "Birthday cannot be null" else error =
                getAgeWithErrorBar(it!!)
        }
    }

    return error
}

fun checkDateIsBeforeToday(birthday: String?): String {
    var error = ""
    if(birthday != null && birthday.contains("-")){
        val date = parseStringToDate(birthday)

        birthday.let {
            if (date == null){
                error = "Date cannot be null"
            } else{
                if(date.before( Date())){
                    error = "Date cannot be before today"
                }
            }
        }
    }else{
        val date = parseStringToDateBar(birthday)

        birthday.let {
            if (date == null){
                error = "Date cannot be null"
            } else{
                if(date.before( Date())){
                    error = "Date cannot be before today"
                }
            }
        }
    }

    return error
}

fun checkDni(dni: String?): String {
    val dniLetters = "TRWAGMYFPDXBNJZSQVHLCKE"
    var error = ""

    if (dni != null && dni != "") {
        val regex = Regex(pattern = "(\\d{8})([A-Z])")
        val matched = regex.containsMatchIn(input = dni) && dni.length == 9
        if (matched) {
            val letter = dniLetters[(dni.subSequence(0, dni.length - 1) as String).toInt().rem(23)]
            dni.let {
                error = if (letter == dni[8]) "" else "Invalid DNI"
            }
        } else {
            error = "Invalid DNI"
        }
    } else {
        error = "DNI required"
    }
    return error
}

fun checkEmail(email: String?): String {
    var error = ""
    email.let {
        val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
        error = if (isEmail) "" else "Must be a valid email"
    }
    return error
}

fun checkName(name: String?): String {
    var error = ""
    name.let {
        if (it?.length!! < 4 || it?.length!! > 30) error =
            "The name must be between 4 and 30 characters" else error = ""
    }
    return error
}

fun checkSurname(surname: String?): String {
    var error = ""
    surname.let {
        if (it?.length!! < 4 || it.length > 40) error =
            "The surname must be between 4 and 40 characters" else error = ""
    }
    return error
}

fun checkPhoto(photo: String?): String {
    var error = ""
    //TODO: validación para las imágenes que se intente subir (?)
    photo.let {
        error = ""
    }
    return error
}

fun checkPhone(phone: String?): String {
    var error = ""
    //TODO: validación para números de teléfono
    phone.let {
        val isPhone = android.util.Patterns.PHONE.matcher(it).matches()
        if (isPhone) error = "" else error = "Must be a valid phone"
    }
    return error
}

fun checkHeight(height: Int?): String {
    var error = ""
    height?.let {
        if (it <= 0) error = "The height must be greater than 0 centimeters" else error = ""
    }
    return error
}