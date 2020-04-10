package com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExerciseTrainerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is My exercises Fragment"
    }
    val text: LiveData<String> = _text
}
