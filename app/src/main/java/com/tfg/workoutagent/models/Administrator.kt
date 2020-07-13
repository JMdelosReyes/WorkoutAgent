package com.tfg.workoutagent.models

import java.util.*

data class Administrator(
    var id : String = "DEFAULT_ID",
    var birthday : Date = Date(System.currentTimeMillis()),
    var dni : String = "DEFAULT_DNI",
    var email : String = "DEFAULT_EMAIL",
    var name : String = "DEFAULT_NAME",
    var phone : String = "DEFAULT_PHONE",
    var photo : String = "DEFAULT_PHOTO",
    var role: String = "ADMINISTRATOR",
    var token: String = "TOKEN",
    var surname: String = "DEFAULT_SURNAME") {
}