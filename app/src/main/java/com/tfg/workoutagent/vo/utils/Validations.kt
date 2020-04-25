package com.tfg.workoutagent.vo.utils


fun checkBirthday(birthday:String?) : String{
    var error = ""
    birthday.let {
        val date = parseStringToDate(it)
        if(date == null) error = "Birthday cannot be null" else error =
            getAgeWithError(it!!)
    }
    return error
}

fun checkDni(dni:String?) : String{
    //TODO: Regexpresion de dni de 8 digitos y 1 letra (al menos)
    var error = ""
    dni.let {
        error = ""
    }
    return error
}

fun checkEmail(email : String?) : String {
    var error = ""
    email.let {
        val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
        error = if(isEmail) "" else "Must be a valid email"
    }
    return error
}

fun checkName(name: String?) : String {
    var error = ""
    name.let {
        if (it?.length!! < 4 || it?.length!! > 30) error = "The name must be between 4 and 30 characters" else error = ""
    }
    return error
}

fun checkSurname(surname : String?) : String {
    var error = ""
    surname.let {
        if (it?.length!! < 4 || it.length > 40) error = "The surname must be between 4 and 40 characters" else error = ""
    }
    return error
}

fun checkPhoto(photo: String?): String{
    var error = ""
    //TODO: validación para las imágenes que se intente subir (?)
    photo.let{
        error = ""
    }
    return error
}

fun checkPhone(phone : String?) : String{
    var error = ""
    //TODO: validación para números de teléfono
    phone.let {
        val isPhone = android.util.Patterns.PHONE.matcher(it).matches()
        if(isPhone) error = ""  else error="Must be a valid phone"
    }
    return error
}

fun checkHeight(height: Int?) : String {
    var error = ""
    height?.let { if(it <= 0) error = "The height must be greater than 0 centimeters" else error = "" }
    return error
}