package com.tfg.workoutagent.models

data class Exercise (
    var id: String = "DEFAULT_ID",
    val title:String = "DEFAULT_EXERCISE_TITLE",
    val description:String = "DEFAULT_EXERCISE_DESCRIPTION",
    val photos: MutableList<String> = mutableListOf(),
    val tags: MutableList<String> = mutableListOf()){
}