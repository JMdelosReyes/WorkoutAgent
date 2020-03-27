package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoutineTrainerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Routine Fragment"
    }
    val text: LiveData<String> = _text
}
