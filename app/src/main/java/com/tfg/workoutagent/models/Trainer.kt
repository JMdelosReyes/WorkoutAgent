package com.tfg.workoutagent.models

import java.util.*

data class Trainer(
    var id : String = "DEFAULT_ID",
    var academicTitle:String = "DEFAULT_ACADEMIC_TITLE",
    var birthday : Date = Date(System.currentTimeMillis()),
    var customers : MutableList<Customer> = mutableListOf(),
    var dni : String = "DEFAULT_DNI",
    var email : String = "DEFAULT_EMAIL",
    var name : String = "DEFAULT_NAME",
    var phone : String = "DEFAULT_PHONE",
    var photo : String = "DEFAULT_PHOTO",
    var role: String = "TRAINER",
    var token: String = "TOKEN",
    var surname: String = "DEFAULT_SURNAME") {
}