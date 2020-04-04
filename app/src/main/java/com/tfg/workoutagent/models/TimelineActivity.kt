package com.tfg.workoutagent.models

import java.util.*

data class TimelineActivity(
    var customerId: String = "DEFAULT_ID",
    var customerPhoto : String = "DEFAULT_PHOTO",
    var customerName : String = "DEFAULT_NAME",
    var finishDate : Date = Date(System.currentTimeMillis())
) {
}