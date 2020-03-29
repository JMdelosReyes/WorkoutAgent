package com.tfg.workoutagent.models

data class Exercise(
    var id: String = "DEFAULT_ID",
    var title: String = "DEFAULT_EXERCISE_TITLE",
    var description: String = "DEFAULT_EXERCISE_DESCRIPTION",
    var photos: MutableList<String> = mutableListOf(),
    var tags: MutableList<String> = mutableListOf()
) {
}